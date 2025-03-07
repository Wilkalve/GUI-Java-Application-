package gui_app;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import controller_.GameController;
import model_.Deck;
import model_.GameModel;


/* Main entry class for the program */

public class CrasyEigthsGame {

	public static void main(String[] args) {

		// create new instance of CrasyEigthsGame
	     new CrasyEigthsGame();
	}

	// Default CrasyEigthsGame constructor
	public CrasyEigthsGame() {
		
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
//	      
		 new GameController();
		
	}
	
}
