package model_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import controller_.GameSession;
import controller_.ServerSide;
import gui_app.GameObserver;
import gui_app.UI_Frame;

public class GameModel {

	public enum GameState {
		CARD_PLAYED, CARD_DRAWN, TURN_CHANGED, GAME_OVER, SUIT_SELECTED, GAME_STARTED, OPPONENT_HANDS, GAME_INFO_UPDATED
	}
	private UI_Frame ui_frame;
	private Deck deck;
	private String aboutMessage;
	private String startMessage;
	private List<Player> players;
	private List<Card> discardPile;
	private Card lastPlayedCard;
	private Card discardPileTop;
	private int currentPlayerIndex;
	private boolean gameRunning;
	private boolean clockwise;
	private int numberOfCardsToPick;
	private boolean skipNextPlayer;
	private String currentSuit;
	private List<GameObserver> observers;
	private GameState currentState;
	private String playerName;
	private String title;
	private String message;
	private String start_;
	private String newTurn;
	private String startGameMessage;
	private String gameOver;
	private String nextTurn;
	private String winner;
	private String name;
	private String name1, name2, name3;
	private String startGame;
	private ServerSide serv;
	 private static GameModel instance;

	// Constructor
	public GameModel() {
        this.ui_frame = UI_Frame.getInstance();
        
        this.aboutMessage = "The game is called Crazy Eight Game";
        this.startMessage = "Please select the number of players first.";
        this.title = "About!";
        this.message = "This has not been implemented";
        this.start_ = "Card Game!";
        this.newTurn = "Turn";
        this.startGame = "Start game";
        this.gameOver = " Win the game!";
        this.name1 = "CPU 1";
        this.name2 = "CPU 2";
        this.name3 = "CPU 3";

        this.players = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.gameRunning = false;
        startNewGame();
    }


    
	public void startNewGame() {
		
		System.out.println("Name: ");
	    this.discardPile = new ArrayList<>();
	    this.discardPileTop = null;

	    this.deck = Deck.getInstance();
	    this.deck.resetDeck();

	    // Create players
	    players.clear();
	    players.add(new Player(name, true));
	    players.add(new Player(name1, false)); 
	    players.add(new Player(name2, false));
	    players.add(new Player(name3, false));

	    
	    dealInitialHands();

	    
	    this.discardPileTop = deck.drawCard();
	    if (discardPileTop != null) {
	        discardPile.add(discardPileTop); 
	    } else {
	        throw new IllegalStateException("Error: Unable to initialize discard pile top card.");
	    }

	    this.currentPlayerIndex = 0;
	    this.gameRunning = true;
	    clockwise = true;
	    setCurrentState(GameState.GAME_STARTED);
	}

	
	private void dealInitialHands() {
        for (Player player : players) {
            List<Card> hand = new ArrayList<>();
            for (int i = 0; i < 6 && deck.remainingCards() > 0; i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    hand.add(card);
                }
            }
            player.setHand(hand);
            System.out.println("Number of cards in player's hand: " + hand.size());
        }
    }
	
	// End game
	public void endGame() {
	    gameRunning = false;
	    setCurrentState(GameState.GAME_OVER);
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
	    if (observers == null) {
	        System.out.println("No observers to notify.");
	        return;
	    }
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
		ui_frame.getEastButton().setText(messages.getString("startGame"));
		ui_frame.getLabel().setText(messages.getString("PlayerNumber"));

		this.aboutMessage = messages.getString("aboutMessage");
		this.startMessage = messages.getString("startMessage");
		this.title = messages.getString("dialog.title");
		this.message = messages.getString("dialog.message");
		this.newTurn = messages.getString("turn_changed");
		this.start_ = messages.getString("start_");
		this.startGameMessage = messages.getString("status_");
		this.gameOver = messages.getString("game_over");
		this.nextTurn = messages.getString("not_your_turn");
		this.winner = messages.getString("wins");
		this.startGame = messages.getString("startGame");

		ui_frame.getStatusLabel().setText(this.startGame );
		ui_frame.getStatusLabel().setText(this.winner);
		ui_frame.getStatusLabel().setText(this.nextTurn);
		ui_frame.getStatusLabel().setText(this.newTurn);
		ui_frame.getStatusLabel().setText(this.startGameMessage);
		ui_frame.getStatusLabel().setText(this.start_);
		

	}

	
	public static synchronized GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

	public Card drawCard(Player player) {
	    if (deck.remainingCards() == 0) {
	        reshuffleDiscardPile();
	    }

	    Card card = deck.drawCard();
	    if (card == null) {
	        System.out.println("No card could be drawn.");
	        return null;
	    }

	    setCurrentState(GameState.CARD_DRAWN);
	    return card;
	}

	public void reshuffleDiscardPile() {
        if (discardPile.size() <= 1) {
            System.out.println("Not enough cards to reshuffle the discard pile.");
            return;
        }

        Card topCard = discardPileTop;
        List<Card> toShuffle = new ArrayList<>(discardPile);
        toShuffle.remove(topCard);

        deck.getCards().addAll(toShuffle);
        deck.shuffle();

        discardPile.clear();
        discardPile.add(topCard);
        discardPileTop = topCard;

        System.out.println("Discard pile reshuffled. Deck now has " + deck.remainingCards() + " cards.");
    }
	
	public boolean isValidMove(Card card) {
	    if (card == null) {
	        return false; 
	    }

	    return card.getRank().equals("8") || card.getSuit().equals(currentSuit) ||  card.getRank().equals(discardPileTop.getRank());
	}
	
// Play card
	
	public boolean playCard(Player player, Card card, String chosenSuit) {
	    if (!isValidMove(card)) {
	        System.out.println("Invalid play!");
	        return false;
	    }

	    if (!player.getHand().contains(card)) {
	        System.out.println("Player does not have the specified card.");
	        return false;
	    }

	   
	    player.removeCardFromHand(card);
	   
	    
	    discardPile.add(card);
	    discardPileTop = card;
	    
	    if (!player.isHuman()) { 
	        ui_frame.removeOpponentCard(player); 
	    } else { 
	        ui_frame.updatePlayerCards(); 
	    }
	    cardEffect(player, card);

	    // Update the current suit
	    if (card.getRank().equals("8") && chosenSuit != null) {
	        currentSuit = chosenSuit;
	    } else {
	        currentSuit = card.getSuit();
	    }

	    setCurrentState(GameState.CARD_PLAYED);
	    return true;
	}


	public boolean processCPUTurn() {
	    Player cpu = getCurrentPlayer();
	    System.out.println("Current CPU Player: " + cpu.getName());

	    if (cpu.getHand().isEmpty()) {
	        System.out.println(cpu.getName() + " wins the game!");
	        gameRunning = false;
	        return true;
	    }

	    List<Card> validCards = new ArrayList<>();
	    for (Card card : cpu.getHand()) {
	        if (isValidMove(card)) {
	            validCards.add(card);
	        }
	    }

	    if (validCards.isEmpty()) {
	        System.out.println(cpu.getName() + " has no valid moves and will draw a card.");
	        Card drawnCard = drawCard(cpu);

	        if (drawnCard != null) {
	            cpu.addCardToHand(drawnCard);
	            System.out.println("CPU drew: " + drawnCard);

	            if (isValidMove(drawnCard)) {
	                playCard(cpu, drawnCard, determinePreferredSuit(cpu));
	                return true;
	            }
	        }

	        skipNextPlayer = true;
	        nextPlayer();
	        return false;
	    }

	    
	    Card cardToPlay = validCards.get(0);
	    String chosenSuit = cardToPlay.getRank().equals("8") ? determinePreferredSuit(cpu) : null;

	    playCard(cpu, cardToPlay, chosenSuit);
	    System.out.println(cpu.getName() + " played: " + cardToPlay + 
	                       (chosenSuit != null ? " with chosen suit " + chosenSuit : ""));

	    nextPlayer();
	    return true;
	}


	private void cardEffect(Player player, Card card) {
	    switch (card.getRank()) {
	        case "8":
	            currentSuit = determinePreferredSuit(player);
	            System.out.println(player.getName() + " changed the suit to: " + currentSuit);
	            break;
	        case "2":
	            Player nextPlayer = getNextPlayer();
	            System.out.println(player.getName() + " played a 2. " + nextPlayer.getName() + " must draw 2 cards.");
	            for (int i = 0; i < 2; i++) {
	                Card drawnCard = drawCard(nextPlayer);
	                if (drawnCard != null) {
	                    nextPlayer.addCardToHand(drawnCard);
	                }
	            }
	            break;
	        case "J":
	            System.out.println(player.getName() + " played a Jack. Next player is skipped.");
	            skipNextPlayer = true;
	            break;
	        default:
	            break;
	    }
	}

	public void nextPlayer() {
	    System.out.println("Current Player Index before: " + currentPlayerIndex);
	    System.out.println("Clockwise: " + clockwise + ", Skip Next Player: " + skipNextPlayer);

	    if (skipNextPlayer) {
	        skipNextPlayer = false;
	        currentPlayerIndex = (currentPlayerIndex + (clockwise ? 2 : -2) + players.size()) % players.size();
	        System.out.println("Skipping a player!");
	    } else {
	        currentPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
	    }

	    System.out.println("Current Player Index after: " + currentPlayerIndex);
	    setCurrentState(GameState.TURN_CHANGED);

	    if (!getCurrentPlayer().isHuman()) {
	        processCPUTurn();
	    }
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

	// Calculate score
	 public int calculateScore(Player player) {
	        int score = 0;

	        for (Card card : player.getHand()) {
	            String rank = card.getRank();
	            switch (rank) {
	                case "J":
	                case "Q":
	                case "K":
	                case "A":
	                case "8":
	                    score += 1;
	                    break;
	                default:
	                    try {
	                        score += Integer.parseInt(rank);
	                    } catch (NumberFormatException e) {
	                        System.out.println("Invalid card rank: " + rank);
	                    }
	                    break;
	            }
	        }
	        return score;
	    }

	public void handleForcePickUp(Player player) {
	    int cardsPicked = 0;

	    for (int i = 0; i < numberOfCardsToPick; i++) {
	        Card drawnCard = drawCard(player);
	        if (drawnCard == null) {
	            System.out.println("Deck is empty, cannot draw more cards.");
	            break;
	        }
	        player.addCardToHand(drawnCard);
	        cardsPicked++;
	    }

	    System.out.println("Forced pick-up complete. Cards picked: " + cardsPicked);
	    numberOfCardsToPick = 0;
	}

	

	public String determinePreferredSuit(Player cpu) {

		return determinePreferredSuit(cpu);
	}

	public void selectSuit(String suit) {
		this.currentSuit = suit;
		setCurrentState(GameState.SUIT_SELECTED);
	}

	public void sendMessage(String message) {

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

	public boolean isOpponentHandsVisible() {
		return isOpponentHandsVisible();
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public List<Card> getDiscardPile() {
		return discardPile;
	}

	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public boolean getTurn() {
		return clockwise;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Card getLastPlayedCard() {
		return lastPlayedCard;
	}

	public void setLastPlayedCard(Card lastPlayedCard) {
		this.lastPlayedCard = lastPlayedCard;
	}

	public String getStart_() {
		return start_;
	}

	public void setStart_(String start_) {
		this.start_ = start_;
	}

	public String getNewTurn() {
		return newTurn;
	}

	public void setNewTurn(String newTurn) {
		this.newTurn = newTurn;
	}

	public String getStartGameMessage() {
		return startGameMessage;
	}

	public void setStartGameMessage(String startGameMessage) {
		this.startGameMessage = startGameMessage;
	}

	public String getGameOver() {
		return gameOver;
	}

	public void setGameOver(String gameOver) {
		this.gameOver = gameOver;
	}

	public String getNextTurn() {
		return nextTurn;
	}

	public void setNextTurn(String nextTurn) {
		this.nextTurn = nextTurn;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getStartGame() {
		return startGame;
	}

	public void setStartGame(String startGame) {
		this.startGame = startGame;
	}

	

}
