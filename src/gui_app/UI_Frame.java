package gui_app;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model_.Card;
import model_.Deck;
import model_.GameModel;
import model_.Player;
import model_.GameModel.GameState;

public class UI_Frame extends JFrame implements GameObserver {
	//private ServerSide serv;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8256502900931300466L;
	private static UI_Frame instance;
	private GameModel model;

	private JWindow splashScreen;
	private JMenu fileMenu, helpMenu, subMenu, hostGame, connect;
	private JMenuItem newItem_1, newItem_2, newItem_3, newItem_4, newItem_5, exitItem;
	private JPanel southPanel;
	private JComboBox<String> languageBox;
	private JButton button1, button2, button3, eastButton, westButton;

	private JPanel northPanel, eastPanel, westPanel, top, down, left, right, mainPanel;
	// private JLabel statusLabel;
	private Map<String, JToggleButton> showCardButtons;
	private  JRadioButtonMenuItem  startServerItem, joinGame;
	private  JRadioButtonMenuItem  endServerItem;
	

	private JLabel label;
	private JButton cardLabel;
	private JPanel cardPanel, newPanel, myPanel, playerPanel;
	private JLabel statusLabel, deckLabel, pileLabel;
	private JLabel[] playerScoreLabels;
	private int playerNumber;
	private List<JButton> halfCards;
	private List<JButton> roverCards;
	private List<JButton> fullCards;
	private List<JButton> fullRover;
	private List<JButton> playercard;
	private List<JButton> fTCard;

	private Deck deck;
	private Card pileCard;
	private ResourceBundle messages;
	private JButton sendButton;
	private JPanel chatPanel;
	Locale locale;
	private Player firstPlayer, secondPlayer, thirdPlayer, fourthPlayer;

	public UI_Frame() {
		splashScreen = new JWindow();
		locale = new Locale("French (français)") != null ? new Locale("fr") : Locale.ENGLISH;
		messages = ResourceBundle.getBundle("MessagesBundle", locale);
		
	
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setOpaque(false);
		northPanel = new JPanel(new BorderLayout());
		northPanel.setOpaque(false);
		eastPanel = new JPanel(new BorderLayout());
		eastPanel.setOpaque(false);
		southPanel = new JPanel(new BorderLayout());
		southPanel.setOpaque(false);
		westPanel = new JPanel(new BorderLayout());
		westPanel.setOpaque(false);

		halfCards = new ArrayList<>();
		roverCards = new ArrayList<>();
		fullCards = new ArrayList<>();
		fullRover = new ArrayList<>();
		playercard = new ArrayList<>();
		fTCard = new ArrayList();
		
	}

	// Method to get the single instance of UI_Frame
	public static synchronized UI_Frame getInstance() {
		if (instance == null) {
			instance = new UI_Frame();
		}
		return instance;
	}

	public void setModel(GameModel model) {
		if (this.model != model) {
			if (this.model != null) {
				this.model.removeObserver(this);
			}
			this.model = model;
			if (this.model != null) {
				this.model.addObserver(this);
			}
		}
	}

	public void initializeComponents() {
		setTitle(messages.getString("game_title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(908, 808);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		ImageIcon icon = new ImageIcon("resources/Kd.png");
		setIconImage(icon.getImage());
		//setResizable(false);
		
		Color cardTableGreen = new Color(0, 100, 35);
		getContentPane().setBackground(cardTableGreen);

		setLayout(new BorderLayout());

		setShowCardButtons(new HashMap<>());
		deck = new Deck();

		// Initialize center panel
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);

		// Initialize panels for each player
		PlayersNames();

		// Initialize deck and pile labels
		ImageIcon deckIcon = new ImageIcon("resources/back.png");
		deckLabel = new JLabel(deckIcon);

		pileCard = model.getDiscardPileTop();

		ImageIcon pileIcon = new ImageIcon(pileCard.getFullCardImage());
		pileLabel = new JLabel(pileIcon);

		// Deck and Pile Panel
		JPanel deckPilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 100));
		deckPilePanel.setOpaque(false);
		deckPilePanel.add(deckLabel);
		deckPilePanel.add(pileLabel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;

		// Add horizontal insets to move deck and pile to the center
		gbc.insets = new Insets(0, 50, 0, 50);

		centerPanel.add(deckPilePanel, gbc);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		add(mainPanel, BorderLayout.CENTER);

		String start_ = model.getStart_();
		statusLabel = new JLabel(start_, SwingConstants.CENTER);

		statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
		statusLabel.setForeground(Color.WHITE);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		gbc.anchor = GridBagConstraints.CENTER;
		centerPanel.add(statusLabel, gbc);

		fileMenu();
		setupSouthPanel();
		initializeNorthPanel();
		setLocationRelativeTo(null);
	}

	// Observe the change in the game
	@Override
	public void update(GameState newState) {
		switch (newState) {
		case CARD_DRAWN:
			updatePlayerCards();

			break;
		case CARD_PLAYED:
			updateDiscardPile(pileCard);
			break;
		case GAME_INFO_UPDATED:
			updateGameInfo();
			break;
		case GAME_OVER:
			handleGameOver();
		
			
			break;
		case GAME_STARTED:
			if (!this.isVisible()) {
				initializeComponents();
				this.setVisible(true);
				
			
			}
			break;

		case OPPONENT_HANDS:
			displaySecondPlayerCards();
			addCardsToEastCenter();
			addCardsToWestCenter();
			
			break;
		case SUIT_SELECTED:
			break;
		case TURN_CHANGED:
			String turn = model.getNewTurn();
			updateStatus(turn + model.getCurrentPlayer().getName());
			updateGameStatus();
			break;
		default:
			System.err.println("Unknown GameState: " + newState);
			break;

		}

		revalidate();
		repaint();

	}

	public void fileMenu() {
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		ImageIcon icon = new ImageIcon(
				new ImageIcon("resources/Menb.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		menubar.add(label);

		fileMenu = new JMenu("Menu");
		helpMenu = new JMenu("Help");
	    hostGame = new JMenu("Host Game");
	    connect = new JMenu("Join game");
	    

		newItem_1 = new JMenuItem("How to play");
		newItem_2 = new JMenuItem("New Game");
		newItem_3 = new JMenuItem("Hint");
		newItem_4 = new JMenuItem("Reset");

		subMenu = new JMenu("Game Option");
		newItem_5 = new JMenuItem("About");

		exitItem = new JMenuItem("Exit");

		newItem_5.setMnemonic('A');
		exitItem.setMnemonic('X');

		helpMenu.add(newItem_1);
		fileMenu.add(newItem_2);
		fileMenu.add(subMenu);
		fileMenu.addSeparator();

		subMenu.add(newItem_3);
		subMenu.add(newItem_4);

		fileMenu.add(subMenu);
		fileMenu.add(newItem_5);
		fileMenu.add(exitItem);

		menubar.add(fileMenu);
		menubar.add(helpMenu);
		menubar.add(hostGame);
		menubar.add(connect);
		
		
		
		startServerItem = new JRadioButtonMenuItem("Start Server");
	    endServerItem = new JRadioButtonMenuItem("End Connection");
	    joinGame = new JRadioButtonMenuItem("Connect");
	     
	        
	        ButtonGroup group = new ButtonGroup();
	        group.add(startServerItem);
	        group.add(endServerItem);
	        group.add(joinGame);
	        

	        // Add the JRadioButtonMenuItems to the JMenu
	        hostGame.add(startServerItem);
	        hostGame.add(endServerItem);
	        connect.add(joinGame);

		
		JPanel languagePanel = new JPanel();
		languagePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		String[] languages = { "English (Canada)", "French (français)" };
		languageBox = new JComboBox<>(languages);
		languageBox.setFocusable(false);
		ImageIcon languageIcon = new ImageIcon(
				new ImageIcon("resources/Vector.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
		JLabel globalLabel = new JLabel(languageIcon);
		globalLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		languagePanel.add(globalLabel);
		languagePanel.add(languageBox);

		menubar.add(Box.createHorizontalGlue());
		menubar.add(languagePanel);

	}

	// get the player name from the list
	public void PlayersNames() {
		List<Player> players = model.getPlayers();

		if (!players.isEmpty()) {
			firstPlayer = players.get(0);
			secondPlayer = players.get(1);
			thirdPlayer = players.get(2);
			fourthPlayer = players.get(3);

		}
	}

	// Method to display card north and south when the player click the button
	public void defaultPlayer() {
		JPanel playerCardPanel = displaySecondPlayerCards();
		northPanel.add(playerCardPanel, BorderLayout.CENTER);
		JPanel playerPanel = displayPlayerCards();
		southPanel.add(playerPanel, BorderLayout.CENTER);

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	public void setupSouthPanel() {
		String start = model.getStartGame();
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setOpaque(false);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		// Start Game button on the west
		JPanel westButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		westButtonPanel.setOpaque(false);
		westButton = new JButton(start);
		westButton.setPreferredSize(new Dimension(100, 30));
		westButtonPanel.add(westButton);

		// Chat button on the east
		JPanel eastButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		eastButtonPanel.setOpaque(false);
		eastButton = new JButton("chat");
		eastButton.setPreferredSize(new Dimension(100, 30));
		eastButtonPanel.add(eastButton);

		// Add button panels to the south panel
		southPanel.add(westButtonPanel, BorderLayout.WEST);
		southPanel.add(eastButtonPanel, BorderLayout.EAST);

		// Add the south panel to the main panel
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	// Display card south
	private JPanel displayPlayerCards() {
		JPanel cardContainer = new JPanel(new BorderLayout());
		cardContainer.setOpaque(false);

		// Player name
		JLabel playerNameLabel = new JLabel(firstPlayer.getName(), SwingConstants.CENTER);
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
		playerNameLabel.setForeground(Color.WHITE);
		cardContainer.add(playerNameLabel, BorderLayout.NORTH);

		// Player's cards
	   cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -31, 0));
		cardPanel.setOpaque(false);

		List<Card> hand = firstPlayer.getHand();

		for (int i = 0; i < hand.size(); i++) {
			Card card = hand.get(i);

			JButton halfCard = new JButton(new ImageIcon(card.getHalfCardImage()));
			halfCard.setOpaque(false);
			halfCard.setContentAreaFilled(false);
			halfCard.setBorderPainted(false);
			halfCard.setFocusPainted(false);

			halfCards.add(halfCard);
			playercard.add(halfCard);
			cardPanel.add(halfCard);
		}

		Card fullImage = firstPlayer.getHand().get(0);
		JButton fullCard = new JButton(new ImageIcon(fullImage.getFullCardImage()));
		fullCard.setOpaque(false);
		fullCard.setContentAreaFilled(false);
		fullCard.setBorderPainted(false);
		fullCard.setFocusPainted(false);

		fullCards.add(fullCard);
		cardPanel.add(fullCard);

		cardContainer.add(cardPanel, BorderLayout.CENTER);

		return cardContainer;
	}


	public void initializeNorthPanel() {
	    northPanel = new JPanel(new BorderLayout());
	    northPanel.setBackground(new Color(0, 100, 35));
	    northPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	    JPanel northWestPanel = new JPanel();
	    northWestPanel.setLayout(new BoxLayout(northWestPanel, BoxLayout.Y_AXIS));
	    northWestPanel.setOpaque(false);

	    label = new JLabel("Player Number");
	    label.setBorder(new EmptyBorder(0, 0, 0, 0));
	    label.setFont(new Font("Arial", Font.BOLD, 12));
	    label.setForeground(Color.WHITE);
	    northWestPanel.add(label);

	    JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
	    buttonPanel.setOpaque(false);
	    buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

	    button1 = new JButton("2");
	    button2 = new JButton("3");
	    button3 = new JButton("4");
	    buttonPanel.add(button1);
	    buttonPanel.add(button2);
	    buttonPanel.add(button3);

	    northWestPanel.add(buttonPanel);
	    northPanel.add(northWestPanel, BorderLayout.WEST);

	   
	    JPanel scorePanel = createScorePanel();
	    northPanel.add(scorePanel, BorderLayout.EAST);

	    mainPanel.add(northPanel, BorderLayout.NORTH);

	    mainPanel.revalidate();
	    mainPanel.repaint();
	}

	private JPanel createScorePanel() {
	    JPanel scorePanel = new JPanel(new GridLayout(4, 1, 5, 5));
	    scorePanel.setOpaque(false);
	    scorePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	    playerScoreLabels = new JLabel[4]; 
	    for (int i = 0; i < playerScoreLabels.length; i++) {
	        playerScoreLabels[i] = new JLabel("Score " + (i + 1) + ": 0");
	        playerScoreLabels[i].setFont(new Font("Arial", Font.BOLD, 12));
	        playerScoreLabels[i].setForeground(Color.ORANGE);
	        scorePanel.add(playerScoreLabels[i]);
	    }
	    return scorePanel;
	}

	public void updatePlayerScores(int[] scores) {
	    for (int i = 0; i < playerScoreLabels.length; i++) {
	        playerScoreLabels[i].setText("Score " + (i + 1) + ": " + scores[i]);
	    }
	    mainPanel.revalidate();
	    mainPanel.repaint();
	}

	
	private JPanel displaySecondPlayerCards() {
		JPanel cardContainer = new JPanel(new BorderLayout());
		cardContainer.setOpaque(false);

		// Player name label
		JLabel playerNameLabel = new JLabel(secondPlayer.getName(), SwingConstants.CENTER);
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		playerNameLabel.setForeground(Color.WHITE);
		cardContainer.add(playerNameLabel, BorderLayout.NORTH);

		// Player's card panel
		    top = new JPanel(new FlowLayout(FlowLayout.CENTER, -32, 0));
		   top.setOpaque(false);

		List<Card> hand = secondPlayer.getHand();

		for (int i = 0; i < hand.size(); i++) {
			JButton halfCard = new JButton(new ImageIcon("resources/lback.png"));
			halfCard.setOpaque(false);
			halfCard.setContentAreaFilled(false);
			halfCard.setBorderPainted(false);
			halfCard.setFocusPainted(false);
			

			fTCard.add(halfCard);
			halfCards.add(halfCard);
			top.add(halfCard);
		}

		firstPlayer.getHand().get(0);
		JButton fullCard = new JButton(new ImageIcon("resources/back.png"));
		fullCard.setOpaque(false);
		fullCard.setContentAreaFilled(false);
		fullCard.setBorderPainted(false);
		fullCard.setFocusPainted(false);

		fullCards.add(fullCard);
		fTCard.add(fullCard);
		top.add(fullCard);

		cardContainer.add(top, BorderLayout.CENTER);
		return cardContainer;
	}
	
	
	public void addCardsToEastCenter() {

		right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.setOpaque(false);

		JLabel playerNameLabel = new JLabel(thirdPlayer.getName(), SwingConstants.CENTER);
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
		playerNameLabel.setForeground(Color.WHITE);
		playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		right.add(playerNameLabel);

		right.add(Box.createRigidArea(new Dimension(0, 120)));

		List<Card> hand = thirdPlayer.getHand();

		for (int i = 0; i < hand.size(); i++) {
			JButton halfCard = new JButton(new ImageIcon("resources/lback_2.png"));
			halfCard.setOpaque(false);
			halfCard.setContentAreaFilled(false);
			halfCard.setBorderPainted(false);
			halfCard.setFocusPainted(false);
			halfCard.setAlignmentX(Component.CENTER_ALIGNMENT);

			fTCard.add(halfCard);
			roverCards.add(halfCard);
			right.add(halfCard);

			right.add(Box.createRigidArea(new Dimension(0, -9)));
		}

		firstPlayer.getHand().get(0);
		JButton fullCard = new JButton(new ImageIcon("resources/back_2.png"));
		fullCard.setOpaque(false);
		fullCard.setContentAreaFilled(false);
		fullCard.setBorderPainted(false);
		fullCard.setFocusPainted(false);

		fullRover.add(fullCard);
		right.add(fullCard);
		fullCard.setAlignmentX(Component.CENTER_ALIGNMENT);

		mainPanel.add(right, BorderLayout.EAST);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	

	public void addCardsToWestCenter() {

		left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.setOpaque(false);

		JLabel playerNameLabel = new JLabel(fourthPlayer.getName(), SwingConstants.CENTER);
		playerNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
		playerNameLabel.setForeground(Color.WHITE);
		playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		left.add(playerNameLabel);

		left.add(Box.createRigidArea(new Dimension(0, 120)));

		List<Card> hand = fourthPlayer.getHand();

		for (int i = 0; i < hand.size(); i++) {
			JButton halfCard = new JButton(new ImageIcon("resources/lback_2.png"));
			halfCard.setOpaque(false);
			halfCard.setContentAreaFilled(false);
			halfCard.setBorderPainted(false);
			halfCard.setFocusPainted(false);
			halfCard.setAlignmentX(Component.CENTER_ALIGNMENT);

			fTCard.add(halfCard);
			roverCards.add(halfCard);
			left.add(halfCard);

			left.add(Box.createRigidArea(new Dimension(0, -9)));
		}

		firstPlayer.getHand().get(0);
		JButton fullCard = new JButton(new ImageIcon("resources/back_2.png"));
		fullCard.setOpaque(false);
		fullCard.setContentAreaFilled(false);
		fullCard.setBorderPainted(false);
		fullCard.setFocusPainted(false);
		fullCard.setAlignmentX(Component.CENTER_ALIGNMENT);

		fullRover.add(fullCard);
		left.add(fullCard);

		mainPanel.add(left, BorderLayout.WEST);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	
	public void removeOpponentCard(Player opponent) {
	    // Ensure the opponent has cards in their hand
	    if (opponent.getHand().isEmpty()) {
	        System.out.println(opponent.getName() + " has no cards left.");
	        return;
	    }

	    Card removedCard = opponent.getHand().remove(0);

	    
	    JPanel opponentPanel = null;
	    List<JButton> opponentCardButtons = null;

	    if (opponent.equals(secondPlayer)) {
	        opponentPanel = top;
	        opponentCardButtons = fTCard; 
	    } else if (opponent.equals(thirdPlayer)) {
	        opponentPanel = left;
	        opponentCardButtons = fTCard; 
	    } else if (opponent.equals(fourthPlayer)) {
	        opponentPanel = right;
	        opponentCardButtons = fTCard; 
	    }

	    if (opponentPanel != null && opponentCardButtons != null && !opponentCardButtons.isEmpty()) {
	        JButton buttonToRemove = opponentCardButtons.remove(0); 
	        opponentPanel.remove(buttonToRemove);                   
	        opponentPanel.revalidate();                           
	        opponentPanel.repaint();
	    }

	    System.out.println("Card removed from " + opponent.getName() + ": " + removedCard);
	}


	public void setPileCard(Card card) {
		pileCard = card;
		updatePileCardImage();
	}

	public void updatePileCardImage() {
		if (pileCard != null) {

			ImageIcon cardIcon = new ImageIcon(pileCard.getFullCardImage());
			pileLabel.setIcon(cardIcon);
		} else {

			pileLabel.setIcon(null);
			updateStatus(messages.getString("No_card_pile"));
		}
	}

	public void updateStatus(String message) {
		statusLabel.setText(message);
	}

	// Update the discard pile display
	public void updateDiscardPile(Card topCard) {

		if (pileLabel != null) {
			topCard = model.getDiscardPileTop();
			if (topCard != null) {
				pileLabel.setIcon(new ImageIcon(topCard.getFullCardImage()));
			} else {
				pileLabel.setIcon(null);
			}
		}
	}

	// Update any other relevant game information
	private void updateGameInfo() {
		updateStatus(messages.getString("game_info_updated"));
	}

	private void handleGameOver() {
		Player winner = model.getCurrentPlayer();
		model.getWinner();
		model.getGameOver();
		for (Player player : model.getPlayers()) {
			if (player.getHand().isEmpty()) {
				winner = player;
				break;
			}
		}

		if (winner != null) {

			JOptionPane.showMessageDialog(this, " " + winner.getName() + model.getGameOver(), "",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void updatePlayerCards() {
		List<JButton> halfCards = getPlayercard();
		List<Card> playerHand = getFirstPlayer().getHand();

		for (int i = 0; i < halfCards.size(); i++) {
			JButton button = halfCards.get(i);
			if (i < playerHand.size()) {
				button.setVisible(true);
			} else {
				button.setVisible(false);
			}
		}
	}

	public void updateGameStatus() {

		String currentPlayerStatus = "Current Player: " + model.getCurrentPlayer().getName();

		String topCardStatus = "Top Card: " + model.getDiscardPileTop();

		String lastCpuAction = "";
		if (!model.getCurrentPlayer().isHuman()) {
			Card cpuPlayedCard = model.getLastPlayedCard();
			if (cpuPlayedCard != null) {
				lastCpuAction = " | CPU Played: " + cpuPlayedCard;
			} else {
				lastCpuAction = " | CPU Drew a Card";
			}
		}

		updateStatus(currentPlayerStatus + " | " + topCardStatus + lastCpuAction);
	}
	
	
	
	

	public JButton getSendButton() {
		return sendButton;
	}

	public JPanel getChatPanel() {
		return chatPanel;
	}

	// Method to display messages to the user
	public void displayMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	// Accessor for languageBox variable
	public JComboBox<String> getLanguageBox() {
		return languageBox;
	}

	// Accessor for FileMenu
	public JMenu getFileMenu() {
		return fileMenu;
	}

	// Accessor for HelpMenu
	public JMenu getHelpMenu() {
		return helpMenu;
	}

	// Accessor for NewItem_1
	public JMenuItem getNewItem_1() {
		return newItem_1;
	}

	// Accessor for NewItem_2
	public JMenuItem getNewItem_2() {
		return newItem_2;
	}

	// Accessor for NewItem_3
	public JMenuItem getNewItem_3() {
		return newItem_3;
	}

	// Accessor for NewItem_4
	public JMenuItem getNewItem_4() {
		return newItem_4;
	}

	// Accessor for SubMenu
	public JMenu getSubMenu() {
		return subMenu;
	}

	// Accessor for NewItem_5
	public JMenuItem getNewItem_5() {
		return newItem_5;
	}

	// Accessor for ExitItem
	public JMenuItem getExitItem() {
		return exitItem;
	}

	public JWindow getSplashScreen() {
		return splashScreen;
	}

	public JPanel getSouthPanel() {
		return southPanel;
	}

	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public JButton getButton1() {
		return button1;
	}

	public JButton getButton2() {
		return button2;
	}

	public JButton getButton3() {
		return button3;
	}

	public JButton getEastButton() {
		return eastButton;
	}

	public JButton getWestButton() {
		return westButton;
	}

	public JLabel getLabel() {
		return label;
	}

	public Map<String, JToggleButton> getShowCardButtons() {
		return showCardButtons;
	}

	public void setShowCardButtons(Map<String, JToggleButton> showCardButtons) {
		this.showCardButtons = showCardButtons;
	}


	public JLabel getDeckLabel() {
		return deckLabel;
	}

	public JButton getCardLabel() {
		return cardLabel;
	}

	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public JPanel getCardPanel() {
		return cardPanel;
	}

	public void setCardPanel(JPanel cardPanel) {
		this.cardPanel = cardPanel;
	}

	public JPanel getNewPanel() {
		return newPanel;
	}

	public void setNewPanel(JPanel newPanel) {
		this.newPanel = newPanel;
	}

	public JPanel getMyPanel() {
		return myPanel;
	}

	public void setMyPanel(JPanel myPanel) {
		this.myPanel = myPanel;
	}

	public JPanel getPlayerPanel() {
		return playerPanel;
	}

	public void setPlayerPanel(JPanel playerPanel) {
		this.playerPanel = playerPanel;
	}

	public JPanel getTop() {
		return top;
	}

	public void setTop(JPanel top) {
		this.top = top;
	}

	public JPanel getDown() {
		return down;
	}

	public void setDown(JPanel down) {
		this.down = down;
	}

	public JPanel getLeft() {
		return left;
	}

	public void setLeft(JPanel left) {
		this.left = left;
	}

	public JPanel getRight() {
		return right;
	}

	public void setRight(JPanel right) {
		this.right = right;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JLabel[] getPlayerScoreLabels() {
		return playerScoreLabels;
	}

	public void setPlayerScoreLabels(JLabel[] playerScoreLabels) {
		this.playerScoreLabels = playerScoreLabels;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public void setFirstPlayer(Player firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public void setSecondPlayer(Player secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public Player getThirdPlayer() {
		return thirdPlayer;
	}

	public void setThirdPlayer(Player thirdPlayer) {
		this.thirdPlayer = thirdPlayer;
	}

	public Player getFourthPlayer() {
		return fourthPlayer;
	}

	public void setFourthPlayer(Player fourthPlayer) {
		this.fourthPlayer = fourthPlayer;
	}


	public List<JButton> getHalfcard() {
		return halfCards;
	}

	public void setHaftcard(List<JButton> haftcard) {
		this.halfCards = haftcard;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public List<JButton> getRoverCards() {
		return roverCards;
	}

	public void setRoverCards(List<JButton> roverCards) {
		this.roverCards = roverCards;
	}

	public List<JButton> getFullCards() {
		return fullCards;
	}

	public void setFullCards(List<JButton> fullCards) {
		this.fullCards = fullCards;
	}

	public List<JButton> getFullRover() {
		return fullRover;
	}

	public void setFullRover(List<JButton> fullRover) {
		this.fullRover = fullRover;
	}

	public String getMessages(String key) {
		return messages.getString(key);
	}

	public List<JButton> getPlayercard() {
		return playercard;
	}

	public void setPlayercard(List<JButton> playercard) {
		this.playercard = playercard;
	}

	public List<JButton> getfTCard() {
		return fTCard;
	}

	public void setfTCard(List<JButton> fTCard) {
		this.fTCard = fTCard;
	}

	public JPanel getNorthPanel() {
		return northPanel;
	}

	public JPanel getEastPanel() {
		return eastPanel;
	}

	public JPanel getwestPanel() {
		return westPanel;
	}

	public JMenu getHostGame() {
		return hostGame;
	}

	public void setHostGame(JMenu resetGame) {
		this.hostGame = resetGame;
	}

	public JMenuItem getStartServerItem() {
		return startServerItem;
	}

	public void setStartServerItem(JRadioButtonMenuItem startServerItem) {
		this.startServerItem = startServerItem;
	}

	public JMenuItem getEndServerItem() {
		return endServerItem;
	}

	public void setEndServerItem(JRadioButtonMenuItem endServerItem) {
		this.endServerItem = endServerItem;
	}

	public JRadioButtonMenuItem getJoinGame() {
		return joinGame;
	}

	public void setJoinGame(JRadioButtonMenuItem joinGame) {
		this.joinGame = joinGame;
	}
	
	

}