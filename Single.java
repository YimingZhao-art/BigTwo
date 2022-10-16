/**
 * A sub class of hand. Represent the "single".
 * 
 * @author yimingzhao
 * @version 1
 */
public class Single extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public Single(CardGamePlayer player, CardList cards) {
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
		if ( size() != 1 ) return false;
		return true;
	}
}