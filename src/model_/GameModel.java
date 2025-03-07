package model_;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import gui_app.UI_Frame;

public class GameModel {

	private UI_Frame ui_frame;
	private Deck deck;
	private Player player;
	private static GameModel instance;
	private String aboutMessage;
	private String startMessage;
	private int[] playerScores;

	// Make the constructor private to enforce singleton pattern
	public GameModel() {

		// Initialize the UI_Frame instance
		this.ui_frame = UI_Frame.getInstance();
		this.deck = new Deck();
		this.aboutMessage = "The game is called Crazy Eight Game";
		this.startMessage = "Please select the number of players first.";

	}

	// Make sure only one instance of the game model is created
	public static GameModel getInstance() {
		if (instance == null) {
			instance = new GameModel();
		}
		return instance;
	}

	/* Method to localy change the system language */
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
		ui_frame.getLabel().setText(messages.getString("numberOfPlayers"));
		ui_frame.getEastButton().setText(messages.getString("startGame"));

		this.aboutMessage = messages.getString("aboutMessage");
		this.startMessage = messages.getString("startMessage");
	}


	// take the players number
    public List<Player> createPlayers(int numPlayers) {
        if (numPlayers < 1 || numPlayers > 4) {
            throw new IllegalArgumentException("Number of players must be between 1 and 4.");
        }

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player " + i));
        }
        
        for(Player pl: players) {
        	System.out.println("NumPlayers: " + pl.getName());
        }
        return players;
    }
    
	// Display card
	public void displayCards(List<Player> players) {
	    int numPlayers = players.size();
	    
	    // Each player gets 12 cards
	    int cardsPerPlayer = 12;
	    int totalCardsNeeded = numPlayers * cardsPerPlayer;

	    // Ensure the deck has enough cards
	    if (deck.remainingCards() < totalCardsNeeded) {
	        throw new IllegalArgumentException("Not enough cards in the deck for the specified number of players.");
	    }

	    // Shuffle the deck
	    deck.shuffle();

	    // Distribute the cards to the players
	    for (int i = 0; i < totalCardsNeeded; i++) {
	        Card drawnCard = deck.drawCard();
	        ui_frame.displayCard(drawnCard);

	        int playerIndex = i % numPlayers;
	        players.get(playerIndex).addCardToHand(drawnCard);
	    }

	    // Display the number of cards for each player
	    for (Player player : players) {
	        System.out.println(player.getName() + " has " + player.getHand().size() + " cards.");
	    }

	    // Display remaining cards count
	    System.out.println("Remaining cards in deck: " + deck.remainingCards());
	}


	// Start game
	public void startGame(int numPlayers) {
        List<Player> players = createPlayers(numPlayers);
        displayCards(players);
    }

	public void updatePlayerScore(int playerIndex, int score) {
		playerScores[playerIndex] = score;
	}

	public int getPlayerScore(int playerIndex) {
		return playerScores[playerIndex];
	}

	public Deck getDeck() {
		return deck;
	}

	public String getStartMessage() {
		return startMessage;
	}

	// Exit the game
	public void exitGame() {
		System.exit(0);
	}

	// Set the UI_Frame instance
	public void setUIFrame(UI_Frame ui_frame) {
		this.ui_frame = ui_frame;
	}

	public String getAboutMessage() {
		return aboutMessage;
	}

}
