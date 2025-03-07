package controller_;

import gui_app.UI_Frame;
import model_.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameController implements ActionListener {
    private final GameModel model;
    private final UI_Frame view;
    private String selectedLanguage;
    private  Locale locale;
    private final Map<String, Runnable> actionHandlers = new HashMap<>();
    private int playerCount = 0;

    public GameController() {
        this.model = GameModel.getInstance();
        this.view = UI_Frame.getInstance();
   	  
        
        if (this.model == null || this.view == null) {
            throw new IllegalStateException("Failed to initialize model or view");
        }

        this.model.setUIFrame(view);
        initializeUI();
        initializeActionHandlers();
        
        model.startGame(4);
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
        
        // Buttons actions listener
        view.getButton1().addActionListener(this);
        view.getButton2().addActionListener(this);
        view.getButton3().addActionListener(this);
        view.getEastButton().addActionListener(this);
        view.getWestButton().addActionListener(this);
        
    }

    private void initializeActionHandlers() {
        // Action handler for menu items
        actionHandlers.put("NewItem_1", this::handleNewItem1);
        actionHandlers.put("NewItem_2", this::handleNewItem2);
        actionHandlers.put("NewItem_3", this::handleNewItem3);
        actionHandlers.put("NewItem_4", this::handleNewItem4);
        actionHandlers.put("NewItem_5", this::handleNewItem5);
        actionHandlers.put("Exit", model::exitGame);

        // Action handler for buttons items
        actionHandlers.put("1", this::handlePlayerSelection1);
        actionHandlers.put("2", this::handlePlayerSelection2);
        actionHandlers.put("3", this::handlePlayerSelection3);
        actionHandlers.put("StartGame", this::handleStartGame);
        actionHandlers.put("Chat", this::handleChat);
    }

  
    private void handleLanguageChange() {
        selectedLanguage = (String) view.getLanguageBox().getSelectedItem();
        locale = selectedLanguage.equals("French (français)") ? new Locale("fr") : Locale.ENGLISH;
        model.changeLanguage(locale);
           
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("comboBoxChanged".equals(command)) {
            handleLanguageChange();
        } else {
            Runnable action = actionHandlers.get(command);
            if (action == null) {
                System.out.println("Unknown command: " + command);
            } else {
                action.run();
            }
        }
    }
    
    private void handleNewItem1() {
        // Handle action for NewItem_1
    }

    private void handleNewItem2() {
        // Handle action for NewItem_2
    }

    private void handleNewItem3() {
        // Handle action for NewItem_3
    }

    private void handleNewItem4() {
        // Handle action for NewItem_4
    }

    private void handleNewItem5() {
        JOptionPane.showMessageDialog(null, model.getAboutMessage(), "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handlePlayerSelection1() {
        System.out.println("2 players selected");
        playerCount = 2;
        view.changeButtonBackground(view.getButton1(), Color.ORANGE);
        view.changeButtonBackground(view.getButton2(), null);
        view.changeButtonBackground(view.getButton3(), null);
    }

    private void handlePlayerSelection2() {
        System.out.println("3 players selected");
        playerCount = 3;
        view.changeButtonBackground(view.getButton1(), null);
        view.changeButtonBackground(view.getButton2(), Color.YELLOW);
        view.changeButtonBackground(view.getButton3(), null);
    }

    private void handlePlayerSelection3() {
        System.out.println("4 players selected");
        playerCount = 4;
        view.changeButtonBackground(view.getButton1(), null);
        view.changeButtonBackground(view.getButton2(), null);
        view.changeButtonBackground(view.getButton3(), Color.MAGENTA);
    }

    private void handleStartGame() {
        if (playerCount > 0) {
            System.out.println("Starting game with " + playerCount + " players.");
            view.setUpCards(playerCount);
            // Additional game start logic here
        } else {
            JOptionPane.showMessageDialog(null, model.getStartMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChat() {
        // Implement chat functionality
    }
}
