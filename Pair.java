/**
 * A sub class of hand. Represent the "pair".
 * 
 * @author yimingzhao
 * @version 1
 */
public class Pair extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7735787877198652522L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
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
		if ( size() == 2 ) {
			if ( getCard(0).getRank() == getCard(1).getRank() ) return true; //two cards are the same
			return false;
		}
		return false;
	}
}