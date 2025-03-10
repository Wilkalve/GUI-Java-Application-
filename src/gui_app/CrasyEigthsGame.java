package gui_app;

import java.util.ArrayList;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller_.GameController;
import model_.GameModel;
import model_.GameModel.GameState;
import model_.Player;



/* Main entry class for the program */

public class CrasyEigthsGame {

	public static void main(String[] args) {

		// create new instance of CrasyEigthsGame
	     new CrasyEigthsGame();
	}

	// Default CrasyEigthsGame constructor
	public CrasyEigthsGame() {
//		
//		SplashScreen splash = new SplashScreen();
//        splash.setVisible(true);
//
//        for (int i = 0; i <= 100; i++) {
//            final int percent = i;
//            try {
//                SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                        splash.setProgress(percent);
//                    }
//                });
//                Thread.sleep(40);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        splash.dispose();
        
		 //Create the GameModel (but don't add players yet!)
		  //Create the GameModel
        GameModel model = new GameModel();

        // Get the UI_Frame instance
        UI_Frame view = UI_Frame.getInstance();

        // Add the view as an observer of the model
        model.addObserver(view);

        // Prompt for player name using JOptionPane on the View's frame
        String playerName = JOptionPane.showInputDialog(view,
                "Enter your name:", "Crazy Eights", JOptionPane.QUESTION_MESSAGE);

        // Handle null or empty name (provide a default)
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }

        // Add Players
        Player player1 = new Player(playerName);
        Player cpu1 = new Player("CPU1");
        Player cpu2 = new Player("CPU2");
        Player cpu3 = new Player("CPU3");

        model.addPlayer(player1);
        model.addPlayer(cpu1);
        model.addPlayer(cpu2);
        model.addPlayer(cpu3);

        // Set the model in the view !
        view.setModel(model);

        // model and players are set up, THEN set the game state
        model.setCurrentState(GameState.GAME_STARTED);

        

        // Create the controller 
        new GameController(model, view);
		

	}

}
