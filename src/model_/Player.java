package model_;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Player {
	private GameModel model;
    private String name;
    private List<Card> hand;
    private int score;
    private boolean isHuman;

    // Constructor
    public Player(String name, boolean isHuman) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isHuman = isHuman; 
        this.score = 0;
    }

    // Add card to player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    //Remove card from player's hand
    public void removeCardFromHand(Card card) {
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

    public boolean isHuman() {
        return isHuman;
    }

    public String determinePreferredSuit(Deck deck) {
        String[] suits = deck.getSuit();
        int[] suitCounts = new int[suits.length];
        String currentSuit = model.getCurrentSuit();

        for (Card card : hand) {
            for (int i = 0; i < suits.length; i++) {
                if (card.getSuit().equals(suits[i])) {
                    suitCounts[i]++;
                    break;
                }
            }
        }

        for (int i = 0; i < suits.length; i++) {
            if (suits[i].equals(currentSuit) && suitCounts[i] > 0) {
                return suits[i];
            }
        }

        int maxIndex = 0;
        for (int i = 1; i < suitCounts.length; i++) {
            if (suitCounts[i] > suitCounts[maxIndex]) {
                maxIndex = i;
            }
        }
        return suits[maxIndex];
    }

}
