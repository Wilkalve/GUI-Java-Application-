package gui_app;

import model_.GameModel.GameState;

public interface GameObserver {
	
	void update(GameState newState);
}
