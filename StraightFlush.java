/**
 * A sub class of hand. Represent the "straightflush".
 * 
 * @author yimingzhao
 * @version 1
 */
public class StraightFlush extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7319311705716526303L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
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
			int[] rankList = new int[5];
			for ( int i = 0; i < 5; i++ ) {
				if ( getCard(i).getRank() < 2 ) rankList[i] = getCard(i).getRank()+13;
				else rankList[i] = getCard(i).getRank();
				if ( i > 0 ) {
					if ( rankList[i] - rankList[i-1] != 1 ) return false; //arithmetic
					if ( getCard(i).getSuit() != getCard(i-1).getSuit() ) return false; //flush
				}
			}
			return true;
		}
		return false;
	}
}