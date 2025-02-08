package gui_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UI_Frame extends JFrame {

	private static final long serialVersionUID = 1345678976544L;
	
	
	
	/*Default constructor */
	UI_Frame() {


		// Initialize the frame
		setTitle("CRAZY EIGHT GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(808, 708);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// change default image icon
		ImageIcon icon = new ImageIcon("resources/Kd.png");
		setIconImage(icon.getImage());
		setResizable(false);
		Color cardTableGreen = new Color(0, 100, 0);
		getContentPane().setBackground(cardTableGreen);
		
		// Methods
		fileMenu();
		cardDeck();
		playerCard();
		scoreboardPanel();
		
	
	
		setLocationRelativeTo(null);
		setVisible(true);

	}
	

	/* File menu */
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
		JMenu fileMenu = new JMenu("Menu");
		JMenu helpMenu = new JMenu("Help");

		JMenuItem newItem_1 = new JMenuItem("How to play");
		JMenuItem newItem_2 = new JMenuItem("New Game");
		JMenuItem newItem_3 = new JMenuItem("Game Option");
		JMenuItem newItem_4 = new JMenuItem("Languages");

		JMenu subMenu = new JMenu("Game Option");
		JMenuItem newItem_5 = new JMenuItem("About");
		JMenuItem exitItem = new JMenuItem("Exit");

		// actions listener
		newItem_5.setMnemonic('A');
		newItem_5.addActionListener(event -> JOptionPane.showMessageDialog(this, "The game is called Crasy Eight Game",
				"About", JOptionPane.PLAIN_MESSAGE));

		exitItem.setMnemonic('x');
		exitItem.addActionListener(event -> System.exit(0));

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
		JComboBox<String> languageBox = new JComboBox<>(languages);
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
	
	
	// Player panel
	public void playerCard() {

		JButton button = new JButton("START GAME");
		button.setFont(new Font("Sans Serif", Font.BOLD, 12));
	
		button.setPreferredSize(new Dimension(80, 50));

		// Chatbox panel
		JButton chatBox = new JButton("Chat Box");
		chatBox.setFont(new Font("Sans Serif", Font.BOLD, 12));
		chatBox.setForeground(Color.BLACK);
		chatBox.setPreferredSize(new Dimension(80, 50));

		// Add an action listener to the chatBox
		chatBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				chatBox();
				
			}
		});

	

		// Create a panel with BorderLayout  to place the button 
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setOpaque(false);
		
		//Start button
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.setOpaque(false);
		leftPanel.add(button);
		
		// Chat box panel
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setOpaque(false);
		rightPanel.add(chatBox);
		
		//Center panel
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		centerPanel.setBorder(new EmptyBorder(35, 45, 30, 30));
		
		
		// Card panel
		for (int i = 0; i < 11; i++) {
			JButton temp = new JButton(new ImageIcon("resources/l2c.png"));
			temp.setBorder(null);
			temp.setContentAreaFilled(false);
			temp.setBorderPainted(false);
			temp.setAlignmentX(Component.CENTER_ALIGNMENT);
			centerPanel.add(temp);
			centerPanel.add(Box.createVerticalStrut(4));
			
		}

		JButton cardButton = new JButton(new ImageIcon("resources/1c.png"));
		cardButton .setBorder(null);
		cardButton.setOpaque(false);
		cardButton.setContentAreaFilled(false);
		cardButton.setBorderPainted(false);
		cardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// add button to the center
		centerPanel.add(cardButton);

		
		// Add sub-panels to the main buttonPanel
	    buttonPanel.add(leftPanel, BorderLayout.WEST);
	    buttonPanel.add(centerPanel, BorderLayout.CENTER);
	    buttonPanel.add(rightPanel, BorderLayout.EAST);
	    
	    
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	}
	
	
	
	/*  Method for center panel */
	public void cardDeck() {

		JPanel cardDeck = new JPanel(new BorderLayout());
		cardDeck.setOpaque(false);

		// Center panel for the card piles with GridBagLayout
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setOpaque(false);

		// East panel
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		eastPanel.setOpaque(false);
		eastPanel.setBorder(new EmptyBorder(90, 200, 90, 200));

		// West panel
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		westPanel.setOpaque(false);
		westPanel.setBorder(new EmptyBorder(90, 200, 90, 200));

	
		
		JButton deck = new JButton(new ImageIcon("resources/Kd.png"));
	
		deck.setBorder(null);
		deck.setOpaque(false);
		deck.setContentAreaFilled(false);
		deck.setBorderPainted(false);
		deck.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		// Card panel
		for (int i = 0; i < 11; i++) {
			JButton temp = new JButton(new ImageIcon("resources/lback_2.png"));
			temp.setBorder(null);
			temp.setContentAreaFilled(false);
			temp.setBorderPainted(false);
			eastPanel.add(temp);
			eastPanel.add(Box.createVerticalStrut(0));
		}

		JButton eastButton = new JButton(new ImageIcon("resources/back_2.png"));
		eastButton.setBorder(null);
		eastButton.setOpaque(false);
		eastButton.setContentAreaFilled(false);
		eastButton.setBorderPainted(false);
		eastPanel.add(eastButton);
		

		// Card panel
		for (int i = 0; i < 11; i++) {
			JButton temp = new JButton(new ImageIcon("resources/lback_2.png"));
			temp.setBorder(null);
			temp.setContentAreaFilled(false);
			temp.setBorderPainted(false);
			westPanel.add(temp);
			westPanel.add(Box.createVerticalStrut(0));
		}

		// West button
		JButton westButton = new JButton(new ImageIcon("resources/back_2.png"));
		westButton.setBorder(null);
		westButton.setOpaque(false);
		westButton.setContentAreaFilled(false);
		westButton.setBorderPainted(false);
		westPanel.add(westButton);

		JButton newButton = new JButton(new ImageIcon("resources/back.png"));
		newButton.setBorder(null);
		newButton.setOpaque(false);
		newButton.setContentAreaFilled(false);
		newButton.setBorderPainted(false);

		// Panel alignments
		centerPanel.add(deck);
		centerPanel.add(Box.createHorizontalStrut(22));
		centerPanel.add(newButton);

		// Use Box to help with alignment
		Box eastBox = Box.createVerticalBox();
		eastBox.add(Box.createVerticalGlue());
		eastBox.add(eastPanel);
		eastBox.add(Box.createVerticalGlue());

		Box westBox = Box.createVerticalBox();
		westBox.add(Box.createVerticalGlue());
		westBox.add(westPanel);
		westBox.add(Box.createVerticalGlue());

		// Add sub-panels to the main buttonPanel
		cardDeck.add(eastBox, BorderLayout.EAST);
		cardDeck.add(centerPanel, BorderLayout.CENTER);
		cardDeck.add(westBox, BorderLayout.WEST);

		getContentPane().add(cardDeck, BorderLayout.CENTER);
	}

	
	// Score panel
	public void scoreboardPanel() {

		// Create scoreboard panel
		JPanel scoreboardPanel = new JPanel();
		scoreboardPanel.setOpaque(false);
		scoreboardPanel.setPreferredSize(new Dimension(80, 30));
		scoreboardPanel.setLayout(new GridLayout(1, 3));

		String player1Score = "0";
		// Create player score labels
		JLabel player1Label = new JLabel("Player 1 Score: " + player1Score, JLabel.CENTER);
		player1Label.setForeground(Color.WHITE);
		player1Label.setFont(new Font("Ariel", Font.BOLD, 16));

		String player2Score = "1";
		JLabel player2Label = new JLabel("Player 2 Score: " + player2Score, JLabel.CENTER);
		player2Label.setForeground(Color.WHITE);
		player2Label.setFont(new Font("Ariel", Font.BOLD, 16));

		String player3Score = "0";
		JLabel player3Label = new JLabel("Player 3 Score: " + player3Score, JLabel.CENTER);
		player3Label.setForeground(Color.WHITE);
		player3Label.setFont(new Font("Ariel", Font.BOLD, 16));

		// Add labels to scoreboard panel
		scoreboardPanel.add(player1Label);
		scoreboardPanel.add(player2Label);
		scoreboardPanel.add(player3Label);

		// Create a container panel to hold both the topPanel and scoreboardPanel
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.setOpaque(false);
		containerPanel.add(scoreboardPanel, BorderLayout.SOUTH);

		// Add container panel to background label
		getContentPane().add(containerPanel, BorderLayout.NORTH);
	}

	
	
	// Chat box Method
		public void chatBox() {
			JDialog chatDialog = new JDialog(this, "Chat Box", false);
	        chatDialog.setLayout(new BorderLayout());
	        chatDialog.setSize(350, 300);

	        JTextArea chatArea = new JTextArea(22, 28);
	        chatArea.setEditable(false);
	        JScrollPane scrollPane = new JScrollPane(chatArea);

	        JTextField chatInput = new JTextField();
	        chatInput.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String text = chatInput.getText();
	                chatArea.append("You: " + text + "\n");
	                chatInput.setText("");
	            }
	        });

	        chatDialog.add(scrollPane, BorderLayout.CENTER);
	        chatDialog.add(chatInput, BorderLayout.SOUTH);

	        // Set the location of the dialog to the bottom-right corner of the screen
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = screenSize.width - chatDialog.getWidth();
	        int y = screenSize.height - chatDialog.getHeight();
	        chatDialog.setLocation(x, y);

	        chatDialog.setVisible(true);

	        // Ensure the dialog is disposed when closed
	        chatDialog.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	                chatDialog.dispose();
	            }
	        });
	    }
		
		
		
}