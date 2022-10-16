/**
 * A sub class of hand. Represent the "triple".
 * 
 * @author yimingzhao
 * @version 1
 */
public class Triple extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2109395258622002051L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public Triple(CardGamePlayer player, CardList cards) {
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
		if ( size() == 3 ) {
			if ( getCard(0).getRank() == getCard(1).getRank() && getCard(0).getRank() == getCard(2).getRank() ) return true; //triple cards
			return false;
		}
		return false;
	}
}