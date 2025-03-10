package model_;

import gui_app.UI_Frame;

/*Class card, store the card objects */
public class Card {
	private String suit;
	private String rank;
	private UI_Frame ui;
	private String halfCardImage;
	private String fullCardImage;

	/* Overloaded constructor */
	public Card(String suit, String rank, String haftCard, String fullCard) {
		this.ui = new UI_Frame();
		this.suit = suit;
		this.rank = rank;
		this.halfCardImage = haftCard;
		this.fullCardImage = fullCard;
		ui.setHaftCard(haftCard);
		ui.setFullCard(haftCard);
	}

	public Card() {

	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getHalfCardImage() {
		return halfCardImage;
	}

	public String getFullCardImage() {
		return fullCardImage;
	}

}
