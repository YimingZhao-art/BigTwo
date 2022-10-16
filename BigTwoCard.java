
/**
 * A BigTwoCard class inheriting "Card"
 * 
 * @author yimingzhao
 * @version 1
 */
public class BigTwoCard extends Card {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5822018182891811204L;



	/**
	 * A constructor using the super.
	 * 
	 * @param suit An int from 0-3.
	 * @param rank An int from 0-12.
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	
	
	
	/**
	 * This overrides the compareTo from Card which is from comparable.
	 * 
	 * @param card Another card to be compared with.
	 */
	@Override
	public int compareTo(Card card) { //compare rank first then suit
		if ( this.equals(card) ) return 0;
		else {
			int self = this.getRank(), another = card.getRank();
			if ( self < 2 ) self += 13;
			if ( another < 2 ) another += 13;
			if ( self == another ) {
				if ( this.getSuit() > card.getSuit() ) return 1;
				else return -1;
			}
			else if ( self > another ) return 1;
			else return -1;
		}
	}
}