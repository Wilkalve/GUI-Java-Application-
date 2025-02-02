package gui_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class UI_Frame extends JFrame{

	/**
	 * Generated serialNumber 
	 */
	private static final long serialVersionUID = 1345678976544L;
	//JLabel backgroundLabel;
	JLabel backgroundLabel;
	 	
	
	// UI_Frame  default constructor
	public UI_Frame() {
		
		new JFrame();
		
		    //Initialize the frame
	        setTitle("CRAZY EIGHT GAME");
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(1920, 1080);
	        setExtendedState(JFrame.MAXIMIZED_BOTH);
	        setLocationRelativeTo(null);
	        setResizable(false);
	      
	        // Load background image
	        ImageIcon backgroundImage = new ImageIcon("spadeBackground.jpg");
	        backgroundLabel = new JLabel(backgroundImage);
	        backgroundLabel.setLayout(new BorderLayout());
	        setContentPane(backgroundLabel);
	        
	        // Create UI components
	        fileMenu();
	        playerCard();
	        
	        setVisible(true);    
	}

	// File menu
	public void fileMenu() {
		
				// Create a menu bar
				JMenuBar menubar = new JMenuBar();
				setJMenuBar(menubar);
				
				// Menu bar image
				ImageIcon icon = new ImageIcon(new ImageIcon("Menb.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
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
				newItem_5.addActionListener(event -> 
		        JOptionPane.showMessageDialog(this, "This a game called Crasy Eight Game", "About", JOptionPane.PLAIN_MESSAGE));

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
		        String[] languages = {"English (Canada)", "French (français)"};
		        JComboBox<String> languageBox = new JComboBox<>(languages);
		        languageBox.setFocusable(false);
		        ImageIcon languageIcon = new ImageIcon(new ImageIcon("Vector.png").getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
		        JLabel globalLabel = new JLabel(languageIcon);
		        globalLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		        
		        languagePanel.add(globalLabel);
		        languagePanel.add(languageBox);
		        
		        menubar.add(Box.createHorizontalGlue()); 
		        menubar.add(languagePanel);

	}
	
	public void playerCard() {

		JButton button = new JButton("START GAME");
		button.setFont(new Font("Sans Serif", Font.BOLD, 12));
		button.setForeground(Color.BLACK);
		button.setPreferredSize(new Dimension(80, 50));

		// Add an action listener to the button
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(backgroundLabel, "Button clicked!");
			}
		});

		// Chatbox panel
		JButton chatBox = new JButton("Chat Box");
		chatBox.setFont(new Font("Sans Serif", Font.BOLD, 12));
		chatBox.setForeground(Color.BLACK);
		chatBox.setPreferredSize(new Dimension(80, 50));

		// Add an action listener to the chatBox
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(backgroundLabel, "Button clicked!");
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
		
		// Add sub-panels to the main buttonPanel
	    buttonPanel.add(leftPanel, BorderLayout.WEST);
	    buttonPanel.add(rightPanel, BorderLayout.EAST);
	    
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	}

}
