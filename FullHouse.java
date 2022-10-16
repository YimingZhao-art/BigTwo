/**
 * A sub class of hand. Represent the "fullhouse".
 * 
 * @author yimingzhao
 * @version 1
 */
public class FullHouse extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -597613473172882159L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/**
	 * Return the card type.
	 * 
	 * @return type The type of this specific hand.
	 */
	@Override
	public String getType() {
		return this.getClass().getName();
	}
	/**
	 * Check whether the hand is valid.
	 *
	 * @return validOrNot Whether it is valid or not.
	 */
	@Override
	public boolean isValid() {
		if ( size() == 5 ) {
			int headRank = getCard(0).getRank(), tailRank = getCard(4).getRank();
			int headCount = 0, tailCount = 0;
			for ( int i = 0; i < 5; i++ ) {
				if ( getCard(i).getRank() == headRank ) headCount++;
				if ( getCard(i).getRank() == tailRank ) tailCount++;
			}
			if ( (headCount == 3 && tailCount == 2) || (headCount == 2 && tailCount == 3) ) return true; //3+2 or 2+3
			else return false;
		}
		return false;
	}
}