package model_;

import java.util.List;
import java.util.ArrayList;

class Player {
    private String name;
    private List<Card> hand;

    // Constructor
    public Player(String playerName) {
        this.name = playerName;
        this.hand = new ArrayList<>();
    }

    // Add card to player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    // Check if player's hand is empty
    public Card playCard() {
        if (hand.isEmpty()) {
            return null;
        }
        return hand.remove(hand.size() - 1);
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
}
