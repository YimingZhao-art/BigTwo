/**
 * A sub class of hand. Represent the "flush".
 * 
 * @author yimingzhao
 * @version 1
 */
public class Flush extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1176872676059568463L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
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
			for ( int i = 1; i < 5; i++ ) {
				if ( getCard(i).getSuit() != getCard(i-1).getSuit() ) return false; //whether the suits are consistent
			}
			return true;
		}
		return false;
	}
}