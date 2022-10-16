/**
 * A sub class of "Deck", the cards in it are big two cards.
 * 
 * @author yimingzhao
 * @version 1
 */
public class BigTwoDeck extends Deck {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8254481884293570270L;

	/**
	 * Override the initialize()
	 * 
	 */
	@Override
	public void initialize() {
		removeAllCards();
		for ( int i = 0; i < 4; i++ ) {
			for ( int j = 0; j < 13; j++ ) {
				addCard(new BigTwoCard(i, j)); //add bigtwocard
			}
		}
	}
}