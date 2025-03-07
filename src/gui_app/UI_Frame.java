package gui_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model_.Card;
import model_.Deck;


public class UI_Frame extends JFrame {

	private static final long serialVersionUID = 1345678976544L;
	private static UI_Frame instance;
	private JWindow splashScreen;
	private JLabel[] playerScores;
	private ImageIcon haftCard;
	private ImageIcon FullCard;
   Card card;
	private JPanel scoreboardPanel;
	private JMenu fileMenu, helpMenu, subMenu;
	private JMenuItem newItem_1, newItem_2, newItem_3, newItem_4, newItem_5, exitItem;
	private JPanel left, right, down, top, center, southPanel;
	private JComboBox<String> languageBox;
	private JButton button1, button2, button3, eastButton, westButton;
	private JLabel[] playerScoreLabels;
	private JLabel label, playerLabel, cardLabel;
	private List<JButton> halfCardButtons;

	/* Default constructor */
	public UI_Frame() {

		splashScreen = new JWindow();
		initializeMainframe();

	}

	private void initializeMainframe() {
		// Initialize the frame
		setTitle("CRAZY EIGHT GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(908, 808);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		// change default image icon
		ImageIcon icon = new ImageIcon("resources/Kd.png");
		setIconImage(icon.getImage());
		setResizable(false);

		Color cardTableGreen = new Color(0, 100, 35);
		getContentPane().setBackground(cardTableGreen);

		// Methods
		fileMenu();
		initializePanels();
		initializeButtons();
		setLocationRelativeTo(null);

		setVisible(true);
	}

	// Make sure only one instance of the GUI View is created
	public static UI_Frame getInstance() {
		if (instance == null) {
			instance = new UI_Frame();
		}
		return instance;
	}

	// Method to create the file menu
	public void fileMenu() {
		// Create a menu bar
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		// Menu bar image
		ImageIcon icon = new ImageIcon(
				new ImageIcon("resources/Menb.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
		JLabel label = new JLabel(icon);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		menubar.add(label);

		// Create JMenu
		fileMenu = new JMenu("Menu");
		helpMenu = new JMenu("Help");

		newItem_1 = new JMenuItem("How to play");
		newItem_2 = new JMenuItem("New Game");
		newItem_3 = new JMenuItem("Hint");
		newItem_4 = new JMenuItem("Reset");

		subMenu = new JMenu("Game Option");
		newItem_5 = new JMenuItem("About");

		exitItem = new JMenuItem("Exit");

		// Action listener for About item
		newItem_5.setMnemonic('A');
		exitItem.setMnemonic('X');

		// Add menu items to the File menu
		helpMenu.add(newItem_1);
		fileMenu.add(newItem_2);
		fileMenu.add(subMenu);
		fileMenu.addSeparator();

		// SubMenu components
		subMenu.add(newItem_3);
		subMenu.add(newItem_4);

		fileMenu.add(subMenu);
		fileMenu.add(newItem_5);
		fileMenu.add(exitItem);

		menubar.add(fileMenu);
		menubar.add(helpMenu);

		// Create a panel to hold the combo box and label
		JPanel languagePanel = new JPanel();
		languagePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		// Add components to the panel
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

	public void initializePanels() {
		setLayout(new BorderLayout());

		cardLabel = new JLabel();
        add(cardLabel, BorderLayout.CENTER);
        
		center = new JPanel(new BorderLayout());
		top = new JPanel(new BorderLayout());
		down = new JPanel(new BorderLayout());
		right = new JPanel(new BorderLayout());
		left = new JPanel(new BorderLayout());

		center.setOpaque(false);
		top.setOpaque(false);
		down.setOpaque(false);
		right.setOpaque(false);
		left.setOpaque(false);

		add(center, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		add(down, BorderLayout.SOUTH);
		add(right, BorderLayout.EAST);
		add(left, BorderLayout.WEST);
	}

	private void initializeButtons() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setOpaque(false);
		

	    label = new JLabel("Number of Players");
		label.setBorder(new EmptyBorder(0, 12, 0, 0));
		label.setFont(new Font("Arial", Font.BOLD, 12));
		label.setForeground(Color.WHITE);
		mainPanel.add(label, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 6, 0));
		buttonPanel.setOpaque(false);

		button1 = new JButton("2");
		button2 = new JButton("3");
		button3 = new JButton("4");

		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);

		buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

		mainPanel.add(buttonPanel, BorderLayout.CENTER);
		left.add(mainPanel, BorderLayout.NORTH);

		JPanel newPanel = new JPanel(new BorderLayout());
		newPanel.setOpaque(false);
		
		
		southPanel = new JPanel(new BorderLayout());
		southPanel.setOpaque(false);

		westButton = new JButton("Chat");
		eastButton = new JButton("Start Game");
		

		// Add padding to the buttons
		westButton.setBorder(new EmptyBorder(10, 20, 10, 20));
		eastButton.setBorder(new EmptyBorder(10, 20, 10, 20));

		// Add buttons to the south panel
		southPanel.add(westButton, BorderLayout.EAST);
		southPanel.add(eastButton, BorderLayout.WEST);

		// Add padding to the panel
		southPanel.setBorder(new EmptyBorder(0, 40, 20, 40));

		// Add the south panel to the main frame
		down.add(southPanel, BorderLayout.SOUTH);

	}
	
	 public void displayCard(Card card) {
		 
	        if (card != null) {
	            String imagePath = card.getFullCardImage(); 
	            ImageIcon cardImage = new ImageIcon(imagePath); 
	            cardLabel.setIcon(cardImage);
	        } else {
	            cardLabel.setText("No more cards in the deck!");
	            cardLabel.setIcon(null); 
	        }
	    }
	
	
	public void setUpCards(int numPlayers) {
		// Clear existing cards and scores, but keep the left panel
		center.removeAll();
		top.removeAll();
		down.removeAll();
		right.removeAll();

		// Re-add the south panel with permanent buttons
		down.add(southPanel, BorderLayout.SOUTH);

		cardView();

		// Create or update scoreboard
		if (scoreboardPanel == null) {
			scoreboardPanel = new JPanel(new GridLayout(1, 4, 10, 0));
			scoreboardPanel.setOpaque(false);
			playerScores = new JLabel[4];

			for (int i = 0; i < 4; i++) {
				JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				playerPanel.setOpaque(false);
			    playerLabel = new JLabel("Score " + (i + 1) + ": ");
				playerLabel.setForeground(Color.WHITE);
				playerScores[i] = new JLabel("0");
				playerScores[i].setForeground(Color.WHITE);
				playerPanel.add(playerLabel);
				playerPanel.add(playerScores[i]);
				scoreboardPanel.add(playerPanel);
			}
		}

		// Update visibility of player scores based on number of players
		for (int i = 0; i < 4; i++) {
			scoreboardPanel.getComponent(i).setVisible(i < numPlayers);
		}

		top.add(scoreboardPanel, BorderLayout.NORTH);

		// Add cards based on the number of players
		switch (numPlayers) {
		case 2:
			top.add(createCardPile(null), BorderLayout.CENTER);
			//down.add(createCardPanel(), BorderLayout.CENTER);
			break;
		case 3:
			top.add(createCardPile(null), BorderLayout.CENTER);
			//down.add(createCardPanel(), BorderLayout.CENTER);

			right.setBorder(new EmptyBorder(110, 0, 0, 280));
			right.add(createCardPile(null), BorderLayout.CENTER);
			break;
		case 4:

			top.add(createCardPile(null), BorderLayout.CENTER);
			//down.add(createCardPanel(), BorderLayout.CENTER);
			right.setBorder(new EmptyBorder(110, 0, 0, 280));
			right.add(createCardPile(null), BorderLayout.CENTER);

			left.setBorder(new EmptyBorder(0, 280, 0, 110));
			left.add(createCardPile(null), BorderLayout.CENTER);
			break;
		}

		// Refresh the layout
		revalidate();
		repaint();
	}


	
	public JPanel createDeckCard(String name) {
	    JPanel cardPanel = new JPanel(new GridBagLayout());

	    cardPanel.setOpaque(false);
	    JButton cardButton = new JButton(new ImageIcon(name));
	    System.out.println("card" + name);
	    cardButton.setBorder(null);
	    cardButton.setContentAreaFilled(false);
	    cardButton.setBorderPainted(false);
	    cardPanel.add(cardButton);

	    return cardPanel;
	}
	
	// Card pile
	public JPanel createCardPile(List<String> cardImages) {
	    int count = 0;
	    JPanel cardPanel = new JPanel(new GridBagLayout());

	    for (String fullCard : cardImages) {
	        if (count >= 1) {
	            break;
	        }

	        cardPanel.setOpaque(false);
	        JButton cardButton = new JButton(new ImageIcon(fullCard));
	        cardButton.setBorder(null);
	        cardButton.setContentAreaFilled(false);
	        cardButton.setBorderPainted(false);
	        cardPanel.add(cardButton);

	        count++;
	    }

	    return cardPanel;
	}


	
	public void cardView() {
		// Create a panel for center cards
		JPanel centerCardsPanel = new JPanel(new GridBagLayout());
		centerCardsPanel.setOpaque(false);

		// Create constraints for centering
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 0, 40);

		// Add deck to the center
		//centerCardsPanel.add(setCardImage(haftCard, FullCard), gbc);

		// Add pile to the center
		gbc.gridx = 1;
		gbc.insets = new Insets(0, 40, 0, 0);
		//centerCardsPanel.add(createCardPile(), gbc);

		// Add the center cards panel to the center of the frame
		center.add(centerCardsPanel, BorderLayout.CENTER);
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

	public JPanel getDownPanel() {return down;}

	public JPanel getLeftPanel() {return left;}

	public JPanel getRightPanel() {return right;}

	public JPanel getTopPanel() {return top;}

	public JPanel getCenterPanel() {return center;}

	public JButton getButton1() {return button1;}

	public JButton getButton2() {return button2;}

	public JButton getButton3() {return button3;}
	
	public JButton getEastButton() {return eastButton;}
	
	public JButton getWestButton() {return westButton;}
	
	public JWindow getSplashScreen() {return splashScreen;}
	
	public JLabel[] getPlayerScoreLabels() {return playerScoreLabels;}
	
	public List<JButton> getHalfCardButtons() {return halfCardButtons;}
	
	public JPanel getSoutPanel() {return southPanel;}
	
	public JPanel getScoreboardPanel() {return scoreboardPanel;}
	
	public JLabel getLabel() {return label;}
	
	public JLabel getPlayerLabel() {return playerLabel;}
	
	public void changeButtonBackground(JButton button, Color color) {
        button.setBackground(color); 
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
}
