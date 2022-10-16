/**
 * A sub class of hand. Represent the "quad".
 * 
 * @author yimingzhao
 * @version 1
 */
public class Quad extends Hand{
	/**
	 * 
	 */
	private static final long serialVersionUID = -576715153845113194L;
	/**
	 * Construct using super.
	 *
	 * @param player The player providing the name.
	 * @param cards The hand cards.
	 */
	public Quad(CardGamePlayer player, CardList cards) {
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
			if ( (headCount == 4 && tailCount == 1) || (headCount == 1 && tailCount == 4) ) return true; //1+4 or 4+1
			else return false;
		}
		return false;
	}
}