package controller_;

import model_.Card;
import model_.GameModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;

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
    }
    

    private void initializeUI() {
        addActionListeners();
        view.getButton1().setActionCommand("1");
        view.getButton2().setActionCommand("2");
        view.getButton3().setActionCommand("3");
        view.getEastButton().setActionCommand("StartGame");
        view.getWestButton().setActionCommand("Chat");
       
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
        actionHandlers.put("NewGame", this::handleNewGame);
        actionHandlers.put("Hint", this::handleHint);
        actionHandlers.put("Reset", this::handleReset);
        actionHandlers.put("About", this::handleAbout);
        actionHandlers.put("Exit", this::handleExit);
        actionHandlers.put("SelectPlayers", this::handlePlayerSelection);
        actionHandlers.put("StartGame", this::handleStartGame);
        actionHandlers.put("Chat", this::handleChat);
        actionHandlers.put("DrawCard", this::handleDrawCard);
    }

    private void handleLanguageChange() {
         selectedLanguage = (String) view.getLanguageBox().getSelectedItem();
            locale = selectedLanguage.equals("French (français)") ? new Locale("fr") : Locale.ENGLISH;
            model.changeLanguage(locale);
    }
    
    
    
    private void attachListeners() {
//        view.addDrawCardButtonListener(new DrawCardListener());
//        view.addToggleOpponentHandsButtonListener(new ToggleOpponentHandsListener());
//        view.addNewGameMenuItemListener(new NewGameMenuItemListener());
        
    }

    class DrawCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Example:
            if(model.isGameRunning()) {
                model.drawCard(model.getCurrentPlayer());
                //view.updateView();
            } else {
                JOptionPane.showMessageDialog(view, "Game not started!");
            }

        }
    }
    
    
    class ToggleOpponentHandsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //view.toggleOpponentHands(); // Update the view's state
            //view.updateView(); // Refresh the view
        }
    }

    class NewGameMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.startNewGame(); 
            //view.updateView(); 
        }
    }

    
    public void handleCardSelected(Card selectedCard) {
        // Logic to handle when a card is selected in the UI
        // Validate if the card can be played
        // Update the model
        // Update the view
    }

    private void handleHowToPlay() {
        // model.showHowToPlay();
    }

    private void handleNewGame() {
        // model.startNewGame();
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
       // int playerCount = view.getSelectedPlayerCount();
        // model.setPlayerCount(playerCount);
    }

    private void handleStartGame() {
        // if (model.canStartGame()) {
        //     model.startGame();
        // } else {
        //     view.showErrorMessage(model.getStartMessage());
        // }
    }

    private void handleChat() {
        //String message = view.getChatMessage();
        // model.sendChatMessage(message);
    }

    private void handleDrawCard() {
        model.drawCard(null);
    }
}
