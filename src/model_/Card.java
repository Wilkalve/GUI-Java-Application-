package model_;

/*Class card, store the card objects */
public class Card {
	private String suit;
	private String rank;
	private String halfCardImage;
	private String fullCardImage;

	/* Overloaded constructor */
	public Card(String suit, String rank, String haftCard, String fullCard) {
		this.suit = suit;
		this.rank = rank;
		this.halfCardImage = haftCard;
		this.fullCardImage = fullCard;
	}
	
	/* return a suit of card */
  public String getSuit() {
	  return suit;
  }
  /* set the suit */
	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	/* return the half card image */
	public String getHalfCardImage() {
		return halfCardImage;
	}

	/* return the full card image */
	public String getFullCardImage() {
		return fullCardImage;
	}
	
	@Override
	public String toString() {
	    return getRank() + getSuit();
	}

}
