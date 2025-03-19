package gui_app;

import javax.swing.SwingUtilities;

import controller_.GameController;
import model_.GameModel;
import model_.GameModel.GameState;



/* Main entry class for the program */

public class CrasyEigthsGame {

	public static void main(String[] args) {

		// create new instance of CrasyEigthsGame
	     new CrasyEigthsGame();
	}

	// Default CrasyEigthsGame constructor
	public CrasyEigthsGame() {
		
		SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        for (int i = 0; i <= 100; i++) {
            final int percent = i;
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        splash.setProgress(percent);
                    }
                });
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        splash.dispose();
        
		 
		  //Create the GameModel
        GameModel model = new GameModel();

        // Get the UI_Frame instance
        UI_Frame view = UI_Frame.getInstance();

        // Add the view as an observer of the model
        model.addObserver(view);

        
        // Set the model in the view !
        view.setModel(model);
       

        // model and players are set up
        model.setCurrentState(GameState.GAME_STARTED);

        // Create the controller 
        new GameController(model, view);
		

	}

}
