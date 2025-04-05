package controller_;

import model_.Card;
import model_.GameModel;
import model_.Player;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import game_network.GameClient;
import game_network.GameServer;
import gui_app.UI_Frame;

public class GameController implements ActionListener {
	private static  GameController instance;
	private final GameModel model;
	private UI_Frame view;

	private final Map<String, Runnable> actionHandlers = new HashMap<>();
	private String selectedLanguage;
	private Locale locale;
	private boolean hasPlayerSelected = false; 
	private boolean isDrawCardListenerAdded = false;
	private int selectedPlayerCount;
    private GameClient client;
    private GameServer server;
    private boolean messageShown = false;
	
    /*Initializes the game controller by linking the model and UI, setting up the server,
   client, and interaction handlers for gameplay effects.*/
	public GameController(GameModel model, UI_Frame view) {
		this.model = model;
		this.view = view;
		this.server = new GameServer();
		this.client = GameClient.getInstance();
		this.selectedPlayerCount = 0;

		if (this.model == null || this.view == null) {
			throw new IllegalStateException("Failed to initialize model or view");
		}
		model.setUI_Frame(view);

		initializeActionHandlers();
		initializeUI();
		
		setupCardHoverEffect();
		CardHoverEffect();
		setupEffect();
		fullCardRover();	
	}
	
	 public static synchronized GameController getInstance(GameModel model, UI_Frame view) {
			if (instance == null) {
				instance = new GameController (model, view);
			}
			return instance;
		}
	 
	 /* Handles action events triggered by user interactions with the UI components.*/
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (e.getSource() == view.getLanguageBox()) {
			handleLanguageChange();
			return;
		}

		Runnable action = actionHandlers.get(command);
		if (action != null) {
			action.run();
		} else {
			System.out.println("Unknown command: " + command);
		}

		switch (command) {
		case "1":
	  handleButton1();
		setSelectedPlayerCount(2);
		 
			break;
		case "2":
			 handleButton2();
			setSelectedPlayerCount(3);
			break;
		case "3":
			 handleButton3();
			setSelectedPlayerCount(4);
			break;
		case "Chat":
			handleChat();
			break;
		case "StartGame":
			handleStartGame();
			break;
		case "connect":
			handleHostGame();
			break;
		case "disconnect":
			handleEndConnection();
			break;
		case "joinGame":
			 handlJoinGame();
			 break;
		default:
			System.out.println("Unknown action command: " + command);
		}

	}

	// Set action on specific object on the gui
	private void initializeUI() {
		addActionListeners();
		view.getButton1().setActionCommand("1");
		view.getButton2().setActionCommand("2");
		view.getButton3().setActionCommand("3");
		view.getEastButton().setActionCommand("Chat");
		view.getWestButton().setActionCommand("StartGame");
		view.getStartServerItem().setActionCommand("connect");
		view.getEndServerItem().setActionCommand("disconnect");
		view.getJoinGame().setActionCommand("joinGame");

	}

	// set action listener on gui objects
	private void addActionListeners() {
		// Menu actions listener
		view.getNewItem_1().addActionListener(this);
		view.getNewItem_2().addActionListener(this);
		view.getNewItem_3().addActionListener(this);
		view.getNewItem_4().addActionListener(this);
		view.getNewItem_5().addActionListener(this);
		view.getExitItem().addActionListener(this);
		view.getLanguageBox().addActionListener(this);
		view.getStartServerItem().addActionListener(this);
		view.getEndServerItem().addActionListener(this); 
		view.getJoinGame().addActionListener(this); 
		view.getLabel();

		// Buttons actions listener
		view.getButton1().addActionListener(this);
		view.getButton2().addActionListener(this);
		view.getButton3().addActionListener(this);
		view.getEastButton().addActionListener(this);
		view.getWestButton().addActionListener(this);

	}

	 
	/*sets up a mapping of action strings to their corresponding handler methods.*/
	private void initializeActionHandlers() {
		actionHandlers.put("HowToPlay", this::handleHowToPlay);
		actionHandlers.put("Hint", this::handleHint);
		actionHandlers.put("Reset", this::handleReset);
		actionHandlers.put("About", this::handleAbout);
		actionHandlers.put("Exit", this::handleExit);
		actionHandlers.put("connect", this::handleHostGame);
		actionHandlers.put("disconnect", this::handleEndConnection);
		actionHandlers.put("joinGame", this::handlJoinGame);
		
		
		actionHandlers.put("SelectPlayers", this::handlePlayerSelection);
		actionHandlers.put("StartGame", this::handleStartGame);
		actionHandlers.put("Chat", this::handleChat);
		actionHandlers.put("DrawCard", this::handleDrawCard);
		actionHandlers.put("Button1", this::handleButton1);
		actionHandlers.put("Button2", this::handleButton2);
		actionHandlers.put("Button3", this::handleButton3);

	}

	/*Updates the application's language setting based on the user's selection
	 and applies the corresponding locale to the model.*/
	private void handleLanguageChange() {
		selectedLanguage = (String) view.getLanguageBox().getSelectedItem();
		locale = selectedLanguage.equals("French (français)") ? new Locale("fr") : Locale.ENGLISH;
		model.changeLanguage(locale);
	}

	

	private void handleHowToPlay() {
		// model.showHowToPlay();
	}

	private void handleHint() {
		// model.provideHint();
	}

	private void handleReset() {
		// model.resetGame();
	}

	private void handleAbout() {
		String title = model.getTitle();
		String messg = model.getAboutMessage();
		JOptionPane.showMessageDialog(view, messg, title, JOptionPane.INFORMATION_MESSAGE);

	}

	// coneect to server
	public void handleHostGame() {
		
		server.launchserver();
		
	}

	// Join the game session
	public void handlJoinGame() {
	    client.launchClient();
	        this.selectedPlayerCount = server.getSelectedPlayerNumber();
	}

	// handle end connection
	public void handleEndConnection(){ 
	 server.disconnectServer();
	 
		JOptionPane.showMessageDialog(null, "The server has been disconnected!", "Connection Ended", 
		        JOptionPane.WARNING_MESSAGE
	    );
		
	}
	
	private void handleExit() {
		model.exitGame();
	}

	private void handlePlayerSelection() {
     
	}

	// Display card north, and south
	public void handleButton1() {
		System.out.println("Button clicked: 2 players selected.");
		
	
		hasPlayerSelected = true;
		view.getButton1().setBackground(Color.PINK);
	    
		view.defaultPlayer();
		
		setupCardHoverEffect();
    	CardHoverEffect();
		setupEffect();
		fullCardRover();


	}

	// Display card north, south and west
	public void handleButton2() {
		System.out.println("Button clicked: 3 players selected.");
		
	
		hasPlayerSelected = true;
		
		this.selectedPlayerCount = 3;
		view.getButton2().setBackground(Color.GREEN);
		view.defaultPlayer();
		view.addCardsToWestCenter();
		
		setupCardHoverEffect();
		CardHoverEffect();
		setupEffect();
		fullCardRover();
		

	}

	// Display card north, south, west and east
	public void handleButton3() {
		System.out.println("Button clicked: 4 players selected.");


		hasPlayerSelected = true;
		this.selectedPlayerCount = 4;

		view.getButton3().setBackground(Color.MAGENTA);
		view.addCardsToWestCenter();
		view.addCardsToEastCenter();
		view.defaultPlayer();

		setupCardHoverEffect();
		CardHoverEffect();
		setupEffect();
		fullCardRover();

	}

	// handle the start game
	private void handleStartGame() {
	    if (!hasPlayerSelected) {
	        
	        JOptionPane.showMessageDialog(
	            null, "Please select the number of players before starting the game.",  "Error", 
	            JOptionPane.ERROR_MESSAGE);
	        return; 
	    }

	    String start = model.getStartGameMessage();
	    view.getStatusLabel().setText(start);
	    handlePlay();
	    handleDrawCard();
	}

	// Chat method
 void handleChat() {
		
	}

	private void setupCardHoverEffect() {

		List<JButton> halfCards = view.getHalfcard();

		for (JButton halfCard : halfCards) {
			halfCard.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {

					halfCard.setLocation(halfCard.getX(), halfCard.getY() - 10);
					halfCard.getParent().repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {

					halfCard.setLocation(halfCard.getX(), halfCard.getY() + 10);
					halfCard.getParent().repaint();
				}
			});
		}
	}

	private void CardHoverEffect() {

		List<JButton> halfCards = view.getRoverCards();

		for (JButton halfCard : halfCards) {
			halfCard.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {

					halfCard.setLocation(halfCard.getX() - 10, halfCard.getY());
					halfCard.getParent().repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {

					halfCard.setLocation(halfCard.getX() + 10, halfCard.getY());
					halfCard.getParent().repaint();
				}
			});
		}
	}

	private void fullCardRover() {

		List<JButton> halfCards = view.getFullRover();

		for (JButton halfCard : halfCards) {
			halfCard.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {

					halfCard.setLocation(halfCard.getX() - 10, halfCard.getY());
					halfCard.getParent().repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {

					halfCard.setLocation(halfCard.getX() + 10, halfCard.getY());
					halfCard.getParent().repaint();
				}
			});
		}
	}

	private void setupEffect() {

		List<JButton> halfCards = view.getFullCards();

		for (JButton halfCard : halfCards) {
			halfCard.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {

					halfCard.setLocation(halfCard.getX(), halfCard.getY() - 10);
					halfCard.getParent().repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {

					halfCard.setLocation(halfCard.getX(), halfCard.getY() + 10);
					halfCard.getParent().repaint();
				}
			});
		}
	}
	
	public void handlePlay() {
	    List<JButton> playerCards = view.getPlayercard();
	    List<Card> playerHand = view.getFirstPlayer().getHand();

	    int loopLimit = Math.min(playerCards.size(), playerHand.size());
	    for (int i = 0; i < loopLimit; i++) {
	        JButton cardButton = playerCards.get(i);
	        Card card = playerHand.get(i);

	        // Clear existing listeners
	        for (ActionListener al : cardButton.getActionListeners()) {
	            cardButton.removeActionListener(al);
	        }

	        cardButton.addActionListener(e -> {
	            Player currentPlayer = model.getCurrentPlayer();

	            if (currentPlayer.isHuman()) {
	                if (model.isValidMove(card)) {
	                    boolean cardPlayed = model.playCard(currentPlayer, card, card.getSuit());
	                    if (cardPlayed) {
	                        playerHand.remove(card); 
	                        playerCards.remove(cardButton);

	                        view.updateDiscardPile(card); 
	                        view.updateStatus(view.getMessages("card_played") + " " + card.toString());
	                        view.updatePlayerCards();

	                      view.getHalfcard().remove(0);
	                        
	                        if (model.isGameOver()) {
	                            view.updateStatus(view.getMessages("game_over"));
	                            return;
	                        }

	                       processCpuTurnsWith();
	                       
                           if (!model.getCurrentPlayer().isHuman()) { 
                   	        view.removeOpponentCard(model.getCurrentPlayer()); 
                   	    } else { 
                   	        view.updatePlayerCards(); 
                   	    }
                           
	                    } else {
	                        view.updateStatus(view.getMessages("invalid_move"));
	                    }
	                } else {
	                    view.updateStatus(view.getMessages("invalid_move"));
	                }
	            }
	        });

	        // Enable or disable button based on card validity
	        cardButton.setEnabled(model.isValidMove(card));
	    }
	}


	private void processCpuTurnsWith() {
	    SwingUtilities.invokeLater(() -> {
	        model.processCPUTurn();

	        Card topCard = model.getDiscardPileTop();
	        if (topCard != null) {
	            view.updateDiscardPile(topCard);
	            view.updateStatus(model.getCurrentPlayer().getName() + " played " + topCard);

	            Player currentPlayer = model.getCurrentPlayer();
	            if (!currentPlayer.isHuman()) {
	            
	                view.updateDiscardPile(topCard);
	            }
	        }

	        if (model.getCurrentPlayer().isHuman() && model.isGameRunning()) {
	            view.updateStatus("Your turn! Make your move.");
	            view.updateGameStatus();
	        } else if (model.isGameRunning()) {
	            processCpuTurnsWith(); 
	        }
	    });
	}


	// Handel draw card for players
	private void handleDrawCard() {
	    if (!isDrawCardListenerAdded) {
	        view.getDeckLabel().addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                Player currentPlayer = model.getCurrentPlayer();

	                if (currentPlayer.isHuman()) {
	                    if (currentPlayer.getHand().size() >= 11) {
	                        return;
	                    }

	                    // Draw a card from the deck
	                    Card drawnCard = model.drawCard(currentPlayer);
	                    if (drawnCard == null) {
	                        view.updateStatus(view.getMessages("deck_empty"));
	                        return;
	                    }

	                    // Add the drawn card to the player's hand
	                    currentPlayer.addCardToHand(drawnCard);
	                    view.updateStatus(view.getMessages("card_drawn") + " " + drawnCard);

	                    // Create a button for the new card
	                    JButton newCardButton = new JButton(new ImageIcon(drawnCard.getHalfCardImage()));
	                    newCardButton.setOpaque(false);
	                    newCardButton.setContentAreaFilled(false);
	                    newCardButton.setBorderPainted(false);
	                    newCardButton.setFocusPainted(false);

	                    // Add hover effect
	                    newCardButton.addMouseListener(new MouseAdapter() {
	                        @Override
	                        public void mouseEntered(MouseEvent e) {
	                            newCardButton.setLocation(newCardButton.getX(), newCardButton.getY() - 10);
	                            newCardButton.getParent().repaint();
	                        }

	                        @Override
	                        public void mouseExited(MouseEvent e) {
	                            newCardButton.setLocation(newCardButton.getX(), newCardButton.getY() + 10);
	                            newCardButton.getParent().repaint();
	                        }
	                    });

	                    newCardButton.addActionListener(event -> {
	                    	
	                        if (model.isValidMove(drawnCard)) {
	                            boolean cardPlayed = model.playCard(currentPlayer, drawnCard, drawnCard.getSuit());
	                            if (cardPlayed) {
	                                currentPlayer.getHand().remove(drawnCard); 
	                                view.getPlayercard().remove(newCardButton); 
	                                view.updateDiscardPile(drawnCard);
	                                view.updateStatus(view.getMessages("card_played") + " " + drawnCard);
	                                view.updatePlayerCards(); 
	                                processCpuTurnsWith();
	                                
	                                if (!model.getCurrentPlayer().isHuman()) { 
	                        	        view.removeOpponentCard(model.getCurrentPlayer()); 
	                        	    } else { 
	                        	        view.updatePlayerCards(); 
	                        	    }
	                                
	                            } else {
	                                view.updateStatus(view.getMessages("invalid_move"));
	                            }
	                        } else {
	                            view.updateStatus(view.getMessages("invalid_move"));
	                        }
	                    });

	                    // Add the new card button to the player's UI panel
	                    JPanel southPanel = view.getCardPanel();
	                    SwingUtilities.invokeLater(() -> {
	                        southPanel.add(newCardButton);
	                        southPanel.revalidate();
	                        southPanel.repaint();
	                    });

	                   
	                    view.getPlayercard().add(newCardButton); 
	                    view.updatePlayerCards(); 
	                }
	            }
	        });
	        isDrawCardListenerAdded = true;
	    }
	}

	
	public int getSelectedPlayerCount() {
		return selectedPlayerCount;
	}

	public void setSelectedPlayerCount(int selectedPlayerCount) {
		this.selectedPlayerCount = selectedPlayerCount;
		
	
	}
	
	


}
