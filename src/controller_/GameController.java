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

import gui_app.UI_Frame;

public class GameController implements ActionListener {
	private final GameModel model;
	private final UI_Frame view;
	private final Map<String, Runnable> actionHandlers = new HashMap<>();
	private String selectedLanguage;
	private Locale locale;

	public GameController(GameModel model, UI_Frame view) {
		this.model = model;
		this.view = view;

		if (this.model == null || this.view == null) {
			throw new IllegalStateException("Failed to initialize model or view");
		}
		model.setUI_Frame(view);

		initializeActionHandlers();
		initializeUI();
		attachListeners();


		setupCardHoverEffect();
		CardHoverEffect();
		setupEffect();
		fullCardRover();

	}

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
			break;
		case "2":
			handleButton2();
			break;
		case "3":
			handleButton3();
			break;
		case "Chat":
			view.chatBox();
			handleChat();
			break;
		case "StartGame":
			handleStartGame();
			break;
		default:
			System.out.println("Unknown action command: " + command);
		}

	}

	private void initializeUI() {
		addActionListeners();
		view.getButton1().setActionCommand("1");
		view.getButton2().setActionCommand("2");
		view.getButton3().setActionCommand("3");
		view.getEastButton().setActionCommand("Chat");
		view.getWestButton().setActionCommand("StartGame");

	}

	private void addActionListeners() {
		// Menu actions listener
		view.getNewItem_1().addActionListener(this);
		view.getNewItem_2().addActionListener(this);
		view.getNewItem_3().addActionListener(this);
		view.getNewItem_4().addActionListener(this);
		view.getNewItem_5().addActionListener(this);
		view.getExitItem().addActionListener(this);
		view.getLanguageBox().addActionListener(this);
		view.getLabel();

		// Buttons actions listener
		view.getButton1().addActionListener(this);
		view.getButton2().addActionListener(this);
		view.getButton3().addActionListener(this);
		view.getEastButton().addActionListener(this);
		view.getWestButton().addActionListener(this);

	}

	private void initializeActionHandlers() {
		actionHandlers.put("HowToPlay", this::handleHowToPlay);
		actionHandlers.put("Hint", this::handleHint);
		actionHandlers.put("Reset", this::handleReset);
		actionHandlers.put("About", this::handleAbout);
		actionHandlers.put("Exit", this::handleExit);
		actionHandlers.put("SelectPlayers", this::handlePlayerSelection);
		actionHandlers.put("StartGame", this::handleStartGame);
		actionHandlers.put("Chat", this::handleChat);
		actionHandlers.put("DrawCard", this::handleDrawCard);
		actionHandlers.put("Button1", this::handleButton1);
		actionHandlers.put("Button2", this::handleButton2);
		actionHandlers.put("Button3", this::handleButton3);

	}

	private void handleLanguageChange() {
		selectedLanguage = (String) view.getLanguageBox().getSelectedItem();
		locale = selectedLanguage.equals("French (français)") ? new Locale("fr") : Locale.ENGLISH;
		model.changeLanguage(locale);
	}

	private void attachListeners() {

	}

	public void handleCardSelected(Card selectedCard) {
		// Logic to handle when a card is selected in the UI

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
		// model.showAboutInfo();
	}

	private void handleExit() {
		model.exitGame();
	}

	private void handlePlayerSelection() {

	}

	public void handleButton1() {
		System.out.println("Button clicked 2");
		view.getButton1().setBackground(Color.PINK);

		String title = model.getTitle();
		String message = model.getMessage();
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);

	}

	public void handleButton2() {
		System.out.println("Button clicked 3");
//	   view.addCardsToWestCenter();
		view.getButton2().setBackground(Color.GREEN);

		String title = model.getTitle();
		String message = model.getMessage();
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);

	}

	public void handleButton3() {
		System.out.println("Button clicked 4");
//	   view.addCardsToWestCenter();
//		view.addCardsToEastCenter();
		view.getButton3().setBackground(Color.MAGENTA);

		String title = model.getTitle();
		String message = model.getMessage();
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);

	}

	private void handleStartGame() {

		String start = model.getStartGameMessage();

		view.getStatusLabel().setText(start);
		handlePlay();
		handleDrawCard();

	}

	private void handleChat() {
		System.out.println("Button Press");

		for (ActionListener al : view.getChatInput().getActionListeners()) {
			view.getChatInput().removeActionListener(al);
		}
		view.getChatInput().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = view.getChatInput().getText();
				view.getChatArea().append("You: " + text + "\n");
				view.getChatInput().setText("");
			}
		});

		String text = view.getChatInput().getText().trim();
		if (!text.isEmpty()) {
			view.getChatArea().append("You: " + text + "\n");
			view.getChatInput().setText("");
		}

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

			cardButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Player currentPlayer = model.getCurrentPlayer();

					if (currentPlayer.isHuman()) {

						if (model.isValidMove(card)) {
							boolean cardPlayed = model.playCard(currentPlayer, card, card.getSuit());
							if (cardPlayed) {
								playerHand.remove(card);
								view.updateDiscardPile(card);
								view.updateStatus(view.getMessages("card_played") + " " + card.toString());
								view.updatePlayerCards();

								// Check if the game is over
								if (model.isGameOver()) {
									view.updateStatus(view.getMessages("game_over"));
									return;
								}

								processCpuTurnsWithDelay();
							} else {
								view.updateStatus(view.getMessages("invalid_move"));
							}
						} else {
							view.updateStatus(view.getMessages("invalid_move"));
						}
					}
				}
			});

			if (model.isValidMove(card)) {
				cardButton.setEnabled(true);
			}

		}

	}

	private void processCpuTurnsWithDelay() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				model.processCPUTurn();

				Card topCard = model.getDiscardPileTop();

				if (topCard != null) {
					view.updateDiscardPile(topCard);
					view.updateStatus(model.getCurrentPlayer().getName() + " played " + topCard.toString());

					Player currentPlayer = model.getCurrentPlayer();
					if (!currentPlayer.isHuman()) {
						JPanel cpuCardPanel = null;
						List<JButton> cpuCardButtons = null;

						if (currentPlayer.equals(view.getSecondPlayer())) {
							cpuCardPanel = view.getNewPanel();
							cpuCardButtons = view.getfTCard();
						} else if (currentPlayer.equals(view.getThirdPlayer())) {
							cpuCardPanel = view.getEastPanel();
							cpuCardButtons = view.getfTCard();
						} else if (currentPlayer.equals(view.getFourthPlayer())) {
							cpuCardPanel = view.getwestPanel();
							cpuCardButtons = view.getfTCard();
						}

						if (cpuCardPanel != null && cpuCardButtons != null && !cpuCardButtons.isEmpty()) {
							JButton cardToRemove = cpuCardButtons.remove(0);
							view.removeBackDisplayCPU2(topCard);
							cpuCardPanel.remove(cardToRemove);
							cpuCardPanel.revalidate();
							cpuCardPanel.repaint();
						}
					}
				}

				if (model.getCurrentPlayer().isHuman() && model.isGameRunning()) {
					view.updateStatus("Your turn! Make your move.");
					view.updateGameStatus();
				} else if (!model.getCurrentPlayer().isHuman() && model.isGameRunning()) {
					processCpuTurnsWithDelay();
				}
			}
		});
	}

	private void handleDrawCard() {

		view.getDeckLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Player currentPlayer = model.getCurrentPlayer();

				if (currentPlayer.isHuman()) {

					if (currentPlayer.getHand().size() >= 12) {

					} else {

						Card drawnCard = model.drawCard(currentPlayer);

						if (drawnCard != null) {
							currentPlayer.addCardToHand(drawnCard);
							view.updateStatus(view.getMessages("card_drawn") + " " + drawnCard.toString());

							JButton newCardButton = new JButton(new ImageIcon(drawnCard.getHalfCardImage()));
							newCardButton.setOpaque(false);
							newCardButton.setContentAreaFilled(false);
							newCardButton.setBorderPainted(false);
							newCardButton.setFocusPainted(false);

							newCardButton.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (model.isValidMove(drawnCard)) {
										boolean cardPlayed = model.playCard(currentPlayer, drawnCard,
												drawnCard.getSuit());
										if (cardPlayed) {
											currentPlayer.getHand().remove(drawnCard);
											view.updateDiscardPile(drawnCard);
											view.updateStatus(
													view.getMessages("card_played") + " " + drawnCard.toString());
											view.updatePlayerCards();
											processCpuTurnsWithDelay();
										} else {
											view.updateStatus(view.getMessages("invalid_move"));
										}
									} else {
										view.updateStatus(view.getMessages("invalid_move"));
									}
								}
							});

							JPanel southPanel = view.getSouthPanel();
							southPanel.add(newCardButton);

							southPanel.revalidate();
							southPanel.repaint();
						} else {

							view.updateStatus(view.getMessages("deck_empty"));
						}
					}
				}
			}
		});
	}

}
