package model_;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Stack;

import gui_app.GameObserver;
import gui_app.UI_Frame;

public class GameModel {

    public enum GameState {
        CARD_PLAYED, CARD_DRAWN, TURN_CHANGED, GAME_OVER, 
        SUIT_SELECTED, GAME_STARTED, OPPONENT_HANDS, GAME_INFO_UPDATED
    }

    private UI_Frame ui_frame;
    private Deck deck;
    private String aboutMessage;
    private String startMessage;
    private List<Player> players;
    private List<Card> decks;
    private List<Card> discardPile;
  
    private Card discardPileTop;
    private int currentPlayerIndex;
    private boolean gameRunning;
    private boolean clockwise;
    private int numberOfCardsToPick;
    private boolean skipNextPlayer;
    private String currentSuit;
    private List<GameObserver> observers = new ArrayList<>();
    private GameState currentState;

    // Constructor
    public GameModel() {
        this.aboutMessage = "The game is called Crazy Eight Game";
        this.startMessage = "Please select the number of players first.";
        
        this.players = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        gameRunning = false;
        startNewGame();
    }

    /* Start Game */
    public void startNewGame() {
        deck = Deck.getInstance();
        deck.resetDeck();
        this.numberOfCardsToPick = 0;
        this.skipNextPlayer = false;
        this.clockwise = true;

        dealInitialHands(); 
        discardPileTop = deck.drawCard();
        currentPlayerIndex = 0;
        gameRunning = true;
        this.currentState = GameState.TURN_CHANGED;
    }

      // End game
    public void endGame() {
        gameRunning = false;
    }
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void setCurrentState(GameState newState) {
        this.currentState = newState;
        notifyObservers();
    }

    private void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update(this.currentState);
        }
    }

    /* Method to locally change the system language */
    public void changeLanguage(Locale locale) {
        if (ui_frame == null) {
            throw new IllegalStateException("UI_Frame instance is not set");
        }
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);

        ui_frame.getFileMenu().setText(messages.getString("fileMenu"));
        ui_frame.getHelpMenu().setText(messages.getString("helpMenu"));
        ui_frame.getNewItem_1().setText(messages.getString("howToPlay"));
        ui_frame.getNewItem_2().setText(messages.getString("newGame"));
        ui_frame.getNewItem_3().setText(messages.getString("hint"));
        ui_frame.getNewItem_4().setText(messages.getString("reset"));
        ui_frame.getSubMenu().setText(messages.getString("gameOption"));
        ui_frame.getNewItem_5().setText(messages.getString("about"));
        ui_frame.getExitItem().setText(messages.getString("exit"));
        //ui_frame.getLabel().setText(messages.getString("numberOfPlayers"));
        ui_frame.getEastButton().setText(messages.getString("startGame"));
        ui_frame.getLabel().setText(messages.getString("PlayerNumber"));

        this.aboutMessage = messages.getString("aboutMessage");
        this.startMessage = messages.getString("startMessage");
    }

    private void dealInitialHands() {
        for (Player player : players) {
            List<Card> hand = new ArrayList<>(); 
            for (int i = 0; i < 5; i++) {
                hand.add(deck.drawCard()); 
            }
            hand.add(deck.drawCard()); 
            player.setHand(hand); 
        }
    }

    public Card drawCard(Player player) {
        Card card = deck.drawCard();

        if (card != null) {
            player.addCardToHand(card);
            setCurrentState(GameState.CARD_DRAWN);
        }
        return card;
    }

    public boolean isValidMove(Card card) {
        if (card == null) {
            return false;
        }

        if (card.getRank().equals("8")) {
            return true;
        }

        if (currentSuit != null) {
            return card.getSuit().equals(currentSuit) || card.getRank().equals(discardPileTop.getRank());
        }

        return card.getSuit().equals(discardPileTop.getSuit()) || card.getRank().equals(discardPileTop.getRank());
    }

    public boolean playCard(Player player, Card card, String suit) {
        if (card == null || !isValidMove(card)) {
            return false;
        }

        discardPileTop = card;

        if (card.getRank().equals("8")) {
            currentSuit = suit;
        } else {
            currentSuit = null;
        }

        cardEffect(player, card);
        player.playCard(card);
        setCurrentState(GameState.CARD_PLAYED);

        return true;
    }

    private void cardEffect(Player player, Card card) {
        String rank = card.getRank();

        switch (rank) {
            case "2":
                numberOfCardsToPick += 2;
                break;
            case "4":
                applyCardEffect();
                skipNextPlayer = true;
                break;
            case "A":
                clockwise = !clockwise;
                break;
            case "Q":
                skipNextPlayer = true;
                break;
        }
    }

    private void applyCardEffect() {
        Player nextPlayer = getNextPlayer();

        for (int i = 0; i < 4; i++) {
            Card drawCard = deck.drawCard();
            if (drawCard != null) {
                nextPlayer.addCardToHand(drawCard);
            }
        }
    }

    public void nextPlayer() {
        if (skipNextPlayer) {
            skipNextPlayer = false;
            currentPlayerIndex = (currentPlayerIndex + (clockwise ? 2 : -2) + players.size()) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
        }
        setCurrentState(GameState.TURN_CHANGED);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getNextPlayer() {
        int nextPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
        return players.get(nextPlayerIndex);
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                gameRunning = false;
                setCurrentState(GameState.GAME_OVER);
                return true;
            }
        }

        return false;
    }

    public int calculateScore(Player player) {
        int score = 0;

        for (Card card : player.getHand()) {
            String rank = card.getRank();

            switch (rank) {
                case "J":
                case "Q":
                case "K":
                    score += 10;
                    break;
                case "A":
                    score += 1;
                    break;
                case "8":
                    score += 50;
                    break;
                default:
                    try {
                        score += Integer.parseInt(rank);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid card rank: " + rank + e);
                    }
                    break;
            }
        }

        return score;
    }

    public void handleForcePickUp(Player player) {
        int handSize = player.getHand().size();

        if (handSize >= 12)
            return;

        int cardsToDraw = Math.min(numberOfCardsToPick, 12 - handSize);

        for (int i = 0; i < cardsToDraw; i++) {
            Card drawnCard = drawCard(player);

            if (drawnCard == null) {
                System.out.println("Deck is empty, cannot draw more cards.");
                break;
            }
        }
        numberOfCardsToPick = 0;
    }

    public void selectSuit(String suit) {
        this.currentSuit = suit;
        setCurrentState(GameState.SUIT_SELECTED);
    }

    // Getters and setters as needed
    public void setUI_Frame(UI_Frame ui_frame) {
        this.ui_frame = ui_frame;
    }

    public String getAboutMessage() {
        return aboutMessage;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public Card getDiscardPileTop() {
        return discardPileTop;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public String getCurrentSuit() {
        return currentSuit;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void exitGame() {
        System.exit(0);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Deck getDeck() {
        return deck;
    }

    public Stack<Card> getDiscardPile() {
        return getDiscardPile();
    }

    public boolean isOpponentHandsVisible() {
        return isOpponentHandsVisible();
    }
}
