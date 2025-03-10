package model_;

import java.util.List;
import java.util.ArrayList;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;

    // Constructor
    public Player(String name2) {
        this.name = name2;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    // Add card to player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }
 public Player() {
	
}
    // remove card from player hand
    public void playCard(Card card) {
         hand.remove(card);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }
    
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
