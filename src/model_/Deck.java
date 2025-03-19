package model_;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/* Hold and manage the collection of Card Objects*/

public class Deck {
    private List<Card> cards;
    private static Deck instance;
    private final String[] suits = {"C", "D", "H", "S"};
    private final String[] ranks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "J", "Q", "K", "A"};
    
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    public static Deck getInstance() {
        if (instance == null) {
            instance = new Deck();
        }
        return instance;
    }

    private void initializeDeck() {
        for (String suit : suits) {
            for (String rank : ranks) {
                String cardCode = rank + suit;
                cards.add(new Card("l" + cardCode, "", "resources/l" + cardCode + ".png", "resources/" + cardCode + ".png"));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }

    public int remainingCards() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }
public String[] getSuit() {
	return suits;
}
public String[] getRank() {
	return ranks;
}
    public void resetDeck() {
        cards.clear(); 
        initializeDeck(); 
        shuffle(); 
    }
    
   
    
}

