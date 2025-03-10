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
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 8256502900931300466L;
	private static UI_Frame instance;
	private GameModel model;

	private JWindow splashScreen;
	private JMenu fileMenu, helpMenu, subMenu;
	private JMenuItem newItem_1, newItem_2, newItem_3, newItem_4, newItem_5, exitItem;
	private JPanel southPanel;
	private JComboBox<String> languageBox;
	private JButton button1, button2, button3, eastButton, westButton;

	private JPanel northPanel, eastPanel, westPanel;
	// private JLabel statusLabel;
	private Map<String, JToggleButton> showCardButtons;
	private Map<String, String[]> cpuCards;

	private ImageIcon haftCard;
	private ImageIcon FullCard;
	private JLabel label;

	private JLabel statusLabel, deckLabel, pileLabel;

	private Deck deck;
	private Card pileCard;

	public UI_Frame() {
		splashScreen = new JWindow();
	}

	// Method to get the single instance of UI_Frame
	public static UI_Frame getInstance() {
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

	// Observe the change in the game
	@Override
	public void update(GameState newState) {
		switch (newState) {
		case CARD_DRAWN:
			break;
		case CARD_PLAYED:

			// updateDiscardPile();
			break;
		case GAME_INFO_UPDATED:
			break;
		case GAME_OVER:
			break;
		case GAME_STARTED:
			if (!this.isVisible()) {
				initializeComponents();
				this.setVisible(true);

			}
			break;
		case OPPONENT_HANDS:
			break;
		case SUIT_SELECTED:
			break;
		case TURN_CHANGED:
			break;
		default:
			break;

		}

		revalidate();
		repaint();

	}

	public void initializeComponents() {
		setTitle("CRAZY EIGHT GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(908, 808);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		ImageIcon icon = new ImageIcon("resources/Kd.png");
		setIconImage(icon.getImage());
		setResizable(false);
		Color cardTableGreen = new Color(0, 100, 35);
		getContentPane().setBackground(cardTableGreen);

		setLayout(new BorderLayout());

		setShowCardButtons(new HashMap<>());
		cpuCards = new HashMap<>();

		// Initialize Deck and Pile
		deck = Deck.getInstance();
		deck.resetDeck();
		deck.shuffle();
		pileCard = deck.drawCard();

		// Initialize panels for each player
		northPanel = computerPanel("CPU 1");
		eastPanel = sideComputerPanel("CPU 2", BorderLayout.EAST);
		westPanel = sideComputerPanel("CPU 3", BorderLayout.WEST);
		southPanel = PlayerPanel("Player");

		// Initialize center panel
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);

		// Initialize deck and pile labels
		ImageIcon deckIcon = new ImageIcon("resources/back.png");
		deckLabel = new JLabel(deckIcon);
		deckLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				drawCardFromDeck();
			}
		});
		pileLabel = new JLabel(new ImageIcon(pileCard.getFullCardImage()));

		// Deck and Pile Panel
		JPanel deckPilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		deckPilePanel.setOpaque(false);
		deckPilePanel.add(deckLabel);
		deckPilePanel.add(pileLabel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		gbc.anchor = GridBagConstraints.CENTER;

		// Add horizontal insets to move deck and pile to the center
		gbc.insets = new Insets(0, 50, 0, 50); // Adjust these values as needed

		centerPanel.add(deckPilePanel, gbc);

		statusLabel = new JLabel("Start Game!", SwingConstants.CENTER);
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

		// Add panels to BorderLayout regions
		add(northPanel, BorderLayout.NORTH);
		add(eastPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);
		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);

		// Set preferred sizes for panels
		northPanel.setPreferredSize(new Dimension(getWidth(), 150));
		eastPanel.setPreferredSize(new Dimension(140, getHeight()));
		westPanel.setPreferredSize(new Dimension(140, getHeight()));
		southPanel.setPreferredSize(new Dimension(getWidth(), 150));

		setLocationRelativeTo(null);
	}

	private JPanel computerPanel(String name) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder());

		JLabel nameLabel = new JLabel(name);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		cardPanel.setOpaque(false);
		cardPanel.setBorder(BorderFactory.createEmptyBorder());

		// Add 5 half hidden cards
		for (int i = 0; i < 5; i++) {
			ImageIcon cardIcon = new ImageIcon("resources/lback.png");
			JLabel cardLabel = new JLabel(cardIcon);
			cardLabel.setBorder(BorderFactory.createEmptyBorder());
			cardPanel.add(cardLabel);
		}

		// Add 1 full hidden card
		ImageIcon fullCardIcon = new ImageIcon("resources/back.png");
		JLabel fullCardLabel = new JLabel(fullCardIcon);
		fullCardLabel.setBorder(BorderFactory.createEmptyBorder());
		cardPanel.add(fullCardLabel);

		label = new JLabel("Number of Players");
		label.setBorder(new EmptyBorder(0, 6, 0, 0));
		label.setFont(new Font("Arial", Font.BOLD, 12));
		label.setForeground(Color.WHITE);
		panel.add(label, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 6, 0));
		buttonPanel.setOpaque(false);

		button1 = new JButton("2");
		button2 = new JButton("3");
		button3 = new JButton("4");

		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);

		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 20));

		panel.add(nameLabel, BorderLayout.SOUTH);
		panel.add(cardPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.WEST);

		return panel;
	}

	private JPanel sideComputerPanel(String name, String orientation) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder());

		JLabel nameLabel = new JLabel(name);
		nameLabel.setForeground(Color.WHITE);

		nameLabel.setHorizontalAlignment(
				orientation.equals(BorderLayout.EAST) ? SwingConstants.LEFT : SwingConstants.RIGHT);

		JPanel cardPanel = new JPanel(new GridBagLayout());
		cardPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);

		// Add 5 half hidden cards
		for (int i = 0; i < 5; i++) {
			ImageIcon cardIcon = new ImageIcon("resources/lback_2.png");
			Image image = cardIcon.getImage();
			JLabel cardLabel = new JLabel(new ImageIcon(image));
			gbc.gridy = i;
			gbc.gridx = 0;
			cardLabel.setBorder(BorderFactory.createEmptyBorder());
			cardPanel.add(cardLabel, gbc);
		}

		// Add 1 full hidden card
		ImageIcon fullCardIcon = new ImageIcon("resources/back_2.png");
		Image fullImage = fullCardIcon.getImage();
		JLabel fullCardLabel = new JLabel(new ImageIcon(fullImage));
		gbc.gridy = 5;
		gbc.gridx = 0;
		fullCardLabel.setBorder(BorderFactory.createEmptyBorder());
		cardPanel.add(fullCardLabel, gbc);

		panel.add(nameLabel, BorderLayout.NORTH);
		panel.add(cardPanel, BorderLayout.CENTER);

		return panel;
	}

	// Method to display the opponent hand
	private void toggleCPUCards(String cpu, boolean selected) {
		JPanel panel;
		switch (cpu) {
		case "CPU 1":
			panel = northPanel;
			break;
		case "CPU 2":
			panel = eastPanel;
			break;
		case "CPU 3":
			panel = westPanel;
			break;
		default:
			return;
		}

		JPanel cardPanel = (JPanel) panel.getComponent(1);
		Component[] cards = cardPanel.getComponents();

		Deck deck = Deck.getInstance();
		for (int i = 0; i < cards.length; i++) {
			JLabel cardLabel = (JLabel) cards[i];
			if (selected) {
				Card card = deck.drawCard();
				if (card != null) {
					ImageIcon cardIcon = new ImageIcon(i < 5 ? card.getHalfCardImage() : card.getFullCardImage());
					Image image = cardIcon.getImage();
					cardLabel.setIcon(new ImageIcon(image));
				}
			} else {
				ImageIcon cardIcon = new ImageIcon(i < 5 ? "resources/lback.png" : "resources/back.png");
				Image image = cardIcon.getImage();
				cardLabel.setIcon(new ImageIcon(image));
			}
		}
		panel.revalidate();
		panel.repaint();
	}

	private JPanel PlayerPanel(String name) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder());

		JLabel nameLabel = new JLabel(name);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		cardPanel.setOpaque(false);
		cardPanel.setBorder(BorderFactory.createEmptyBorder());

		Deck deck = Deck.getInstance();
		// Add 5 half cards
		for (int i = 0; i < 5; i++) {
			Card card = deck.drawCard();
			if (card != null) {
				ImageIcon cardIcon = new ImageIcon(card.getHalfCardImage());
				JLabel cardLabel = new JLabel(cardIcon);
				cardLabel.setBorder(BorderFactory.createEmptyBorder());
				cardPanel.add(cardLabel);
			}
		}
		// Add 1 full card
		Card fullCard = deck.drawCard();
		if (fullCard != null) {
			ImageIcon cardIcon = new ImageIcon(fullCard.getFullCardImage());
			JLabel cardLabel = new JLabel(cardIcon);
			cardLabel.setBorder(BorderFactory.createEmptyBorder());
			cardPanel.add(cardLabel);
		}

		westButton = new JButton("Chat");
		eastButton = new JButton("Start Game");

		// Add padding to the panel
		panel.setBorder(new EmptyBorder(0, 40, 20, 40));

		panel.add(nameLabel, BorderLayout.NORTH);
		panel.add(cardPanel, BorderLayout.CENTER);
		panel.add(westButton, BorderLayout.EAST);
		panel.add(eastButton, BorderLayout.WEST);

		return panel;
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

	public void updateStatus(String message) {
		statusLabel.setText(message);
	}

	public void updatePlayerCards(String[] cardNames) {
		updateCards(southPanel, cardNames, true);
	}

	public void updateCPUCards(String cpuName, String[] cardNames) {
		cpuCards.put(cpuName, cardNames);
		JPanel panel = getPanelForCPU(cpuName);
		updateCards(panel, cardNames, false);
	}

	private JPanel getPanelForCPU(String cpuName) {
		switch (cpuName) {
		case "CPU 1":
			return northPanel;
		case "CPU 2":
			return eastPanel;
		case "CPU 3":
			return westPanel;
		default:
			throw new IllegalArgumentException("Invalid CPU name: " + cpuName);
		}
	}

	private void updateCards(JPanel panel, String[] cardNames, boolean show) {
		panel.removeAll();
		for (int i = 0; i < cardNames.length; i++) {
			String displayText = show ? cardNames[i]
					: (i < cardNames.length - 1 ? "resources/back.png" : "resources/back.png");
			JLabel cardLabel = new JLabel(displayText);
			cardLabel.setPreferredSize(new Dimension(90, 120));
			cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
			cardLabel.setVerticalAlignment(SwingConstants.CENTER);
			panel.add(cardLabel);
		}
		panel.revalidate();
		panel.repaint();
	}

	private void drawCardFromDeck() {
		if (deck.remainingCards() > 0) {
			Card newCard = deck.drawCard();
			setPileCard(newCard);
			updatePileCardImage();
			updateStatus("Card drawn from deck");
		} else {
			updateStatus("Deck is empty");
		}
	}

	private void setPileCard(Card card) {
		pileCard = card;
	}

	private void updatePileCardImage() {
		if (pileCard != null) {
			ImageIcon cardIcon = new ImageIcon(pileCard.getFullCardImage());
			pileLabel.setIcon(cardIcon);
		} else {
			pileLabel.setIcon(null);
			statusLabel.setText("No card on the pile");
		}
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

	public ImageIcon getHaftCard() {
		return haftCard;
	}

	public void setHaftCard(String haftCardImage) {

		this.haftCard = new ImageIcon(haftCardImage);
	}

	public ImageIcon getFullCard() {
		return FullCard;
	}

	public void setFullCard(String fullCardImage) {
		FullCard = new ImageIcon(fullCardImage);
	}

	public void setActionListener(ActionListener listener) {
		// Add the listener to all relevant components
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

}