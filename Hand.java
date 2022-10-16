/**
 * A sub class of cardList.
 * 
 * @author yimingzhao
 * @version 1
 */
public abstract class Hand extends CardList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3708415018664975012L;
	private CardGamePlayer player;
	/**
	 * Construct a hand with a player, add cards to hand.cards
	 * 
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for ( int i = 0; i < cards.size(); i++ ) {
			addCard(cards.getCard(i));
		}
		sort();
	}
	/**
	 * Return player.
	 * 
	 * @return player The player in hand.
	 */
	public CardGamePlayer getPlayer() {
		return this.player; //the playername will be used
	}
	/**
	 * Return the decisive card in a hand.
	 * 
	 * @return topCard The decisive card in a hand.
	 */
	public Card getTopCard() {
		if ( getType() != "FullHouse" && getType() != "Quad" ) {
			return getCard(size()-1); //other type are the last one in sort
		}
		else {
			int headRank = getCard(0).getRank(), tailRank = getCard(size()-1).getRank();
			int numOfHead = 0, numOfTail = 0;
			for ( int i = 0; i < size(); i++ ) {
				if ( getCard(i).getRank() == headRank ) numOfHead++;
				else if ( getCard(i).getRank() == tailRank ) numOfTail++;
			}
			if ( numOfHead < numOfTail ) {
				return getCard(size()-1); //2+3 or 1+4 return the last one
			}
			else return getCard(numOfHead-1); //3+2 or 4+1 return the 2or3
		}
	}
	/**
	 * Return the relationship between two hands.
	 * 
	 * @param hand Another hand to be compared with.
	 * @return beatOrNot Whether the hand beats the other or not.
	 */
	public boolean beats(Hand hand) {
		if ( this.getType() == hand.getType() ) {
			if ( this.getType() == "Flush") { //If it's flush, the suits matters more.
				if ( this.getTopCard().getSuit() > hand.getTopCard().getSuit() ) return true;
				else if ( this.getTopCard().getSuit() == hand.getTopCard().getSuit() ) {
					if ( this.getTopCard().compareTo(hand.getTopCard()) > 0 ) return true;
					else return false;
				}
				else {
					return false;
				}
			} //not flush, just use compareTo
			else if ( this.getTopCard().compareTo(hand.getTopCard()) > 0 ) return true;
			else return false;
		}
		else { //descending order
			if ( this.getType() == "StraightFlush" ) return true;
			else if ( hand.getType() == "StraightFlush" ) return false;
			else if ( this.getType() == "Quad" ) return true;
			else if ( hand.getType() == "Quad" ) return false;
			else if ( this.getType() == "FullHouse" ) return true;
			else if ( hand.getType() == "FullHouse" ) return false;
			else if ( this.getType() == "Flush" ) return true;
			else if ( hand.getType() == "Flush" ) return false;
			else if ( this.getType() == "Straight" ) return true;
			else if ( hand.getType() == "Straight" ) return false;
		}
		return false;
	}
	/**
	 * An abstract function about whether the hand is valid.
	 * 
	 * @return validOrNot Whether the hand is valid.
	 */
	public abstract boolean isValid();
	/**
	 * An abstract function about the hand type.
	 * 
	 * @return type The type of a hand.
	 */
	public abstract String getType();
	
}