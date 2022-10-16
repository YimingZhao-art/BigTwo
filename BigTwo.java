import java.util.ArrayList;
import javax.swing.*;
/**
 * This is a sub-class of CardGame. It implements the "Big Two" game.
 * 
 * @author yimingzhao
 * @version 3
 */
public class BigTwo extends JFrame implements CardGame {
	private static final long serialVersionUID = 6094096319307721666L; //serialID
	private int numOfPlayers; //number of player
	private Deck deck; //deck
	private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>(); //playerList
	private ArrayList<Hand> handsOnTable; //handsontable
	private int currentPlayerIdx = -1; //currentplayer
	private BigTwoGUI ui = null; //gui
	private BigTwoClient client = null; //client
	
	/**
	 * This is the constructor of "BigTwo", it adds 4 players to playerList and create a "BigTwoUi" instance.
	 */
	public BigTwo() {
		handsOnTable = new ArrayList<Hand>();
		for ( int i = 0; i < 4; i++ ) {
			playerList.add(new CardGamePlayer());
		}
		ui = new BigTwoGUI(this);
		client = new BigTwoClient(this, ui);
		
	}
	/**
	 * Get the client.
	 * 
	 * @return client
	 */
	public BigTwoClient getClient() {
		return this.client;
	}
	/**
	 * Get the gui.
	 * 
	 * @return GUI
	 */
	public BigTwoGUI getGUI() {
		return this.ui;
	}
	
	/**
	 * Return the number of players.
	 * 
	 * @return numOfPlayers The number of players on table.
	 */
	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}
	/**
	 * Return the deck.
	 * 
	 * @return deck The deck on table.
	 */
	@Override
	public Deck getDeck() {
		return this.deck;
	}
	/**
	 * Return the playerList.
	 * 
	 * @return playerList Arraylist of players.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}
	/**
	 * Return the handsOnTable.
	 * 
	 * @return handsOnTable ArrayList of the hands on table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}
	/**
	 * Clean the handsontable
	 */
	public void cleanTable() {
		this.handsOnTable = new ArrayList<Hand>();
	}
	
	/**
	 * Return the currentPlayerIdx.
	 * 
	 * @return currentPlayerIdx The current player's id.
	 */
	@Override
	public int getCurrentPlayerIdx() {
		return this.currentPlayerIdx;
	}
	/**
	 * Start the game. Clear the players' cards and table. Distribute cards to players and delete from the deck. Check which player to start the game amd start.
	 * 
	 * @param deck The deck we use.
	 */
	@Override
	public void start(Deck deck) {
		for ( int i = 0; i < 4; i++ ) {
			playerList.get(i).removeAllCards();
			//playerList.get(i).setName("Player "+(i+1));
		}
		/*
		for ( int i = 0; i < this.handsOnTable.size(); i++ ) {
			handsOnTable.remove(0);
		}*/
		this.handsOnTable = new ArrayList<Hand>();
		for ( int i = 0; i < 4; i++ ) {
			for ( int j = 0; j < 13; j++ ) {
				playerList.get(i).addCard(deck.getCard(0));
				if ( deck.getCard(0).equals(new BigTwoCard(0, 2)) ) {
					this.currentPlayerIdx = i;
					ui.setActivePlayer(i);
					
				}// suit3 rank2 is diamond 3
				deck.removeCard(0);
			}
		}
		for ( int i = 0; i < 4; i++ ) {
			playerList.get(i).sortCardsInHand();
		}
		ui.enable();
		ui.reset();
		ui.printMsg("Game starts now!!\n");
		ui.promptActivePlayer();
		ui.repaint();

	}
	
	/**
	 * Make a move by change the currentIdx after checkMove. Then set the ui to update table, set the activeplayer.
	 * 
	 * @param playerIdx Id of player.
	 * @param cardIdx Id array of the card.
	 */
	@Override
	public void makeMove(int playerIdx, int[] cardIdx) {
		CardGameMessage msg = new CardGameMessage(6, -1, cardIdx);
		client.sendMessage(msg); //make in TCP only broadcast msg
	}
	/**
	 * Check the move and change currentIdx.
	 * 
	 * @param playerIdx Id of player.
	 * @param cardIdx Id array of the card.
	 */
	@Override
	public void checkMove(int playerIdx, int[] cardIdx) {
		boolean localPrint = false; //illegal information only appear on the local screen
		if ( getClient().getPlayerID() == playerIdx ) localPrint = true;
		
		boolean cardIdxNull = (cardIdx == null);
		boolean tableNull = (this.handsOnTable.isEmpty());
		
		if ( cardIdxNull ) {
			if ( localPrint ) {
				if ( tableNull ) {
					//ui.promptActivePlayer();
					ui.printMsg("{Pass}. Not legal, it's your turn.\n");
					ui.promptActivePlayer();
				}
				else if ( handsOnTable.get(handsOnTable.size()-1).getPlayer() == playerList.get(playerIdx) ) {
					//ui.promptActivePlayer();
					ui.printMsg("{Pass}. Not legal, it's your turn.\n");
					ui.promptActivePlayer();
				}
				else {
					//ui.promptActivePlayer();
					ui.printMsg("{Pass}\n");
					currentPlayerIdx = (currentPlayerIdx+1)%4;
					ui.setActivePlayer(this.currentPlayerIdx);
					ui.promptActivePlayer();
				}
			}
			else {
				if ( tableNull ) {
					//
				}
				else if ( handsOnTable.get(handsOnTable.size()-1).getPlayer() == playerList.get(playerIdx) ) {
					//
				}
				else {
					//ui.promptActivePlayer();
					ui.printMsg("{Pass}\n");
					currentPlayerIdx = (currentPlayerIdx+1)%4;
					ui.setActivePlayer(this.currentPlayerIdx);
					ui.promptActivePlayer();
				}
				
			}
		}
		else {
			Hand temp = composeHand(playerList.get(playerIdx), this.playerList.get(playerIdx).play(cardIdx));
			if ( temp == null ) {
				if ( localPrint ) {
					//ui.promptActivePlayer();
					ui.printMsg("Not a legal move!\n");
					ui.promptActivePlayer();
				}
			}
			else {
				if ( !tableNull && ( !temp.beats(this.handsOnTable.get(this.handsOnTable.size()-1)) && !(handsOnTable.get(handsOnTable.size()-1).getPlayer() == playerList.get(playerIdx)) ) ) {
					if ( localPrint ) {
						//ui.promptActivePlayer();
						ui.printMsg("Not a legal move!\n");
						ui.promptActivePlayer();
					}
				}
				else {
					if ( tableNull && !temp.contains(new BigTwoCard(0, 2)) ) {
						ui.printMsg("Not a legal move!You need to start with diamond 3.\n");
						ui.promptActivePlayer();
					}
					else {
						String string = "";
						for (int i = 0; i < temp.size(); i++) {
							string = string + "[" + temp.getCard(i) + "]";
							if (i % 13 != 0) {
								string = " " + string;
							}
							if (i % 13 == 12 || i == temp.size() - 1) {
								string += "\n";
							}
						} //shown in the message area
						//ui.promptActivePlayer();
						currentPlayerIdx = (currentPlayerIdx+1)%4;
						ui.setActivePlayer(this.currentPlayerIdx);
						ui.printMsg(String.format("{%s} ", temp.getType()));
						ui.printMsg(string);
						this.getHandsOnTable().add(temp);
						playerList.get(playerIdx).removeCards(temp);
						ui.promptActivePlayer();
					}
				}
			}
		}
		
		
		ui.repaint();
		
		if ( endOfGame() ) {
			//display all the cards.
			ui.setActivePlayer(-1);
			//ui.repaint();
			ui.printMsg("\n");
			//display the end information
			ui.printMsg("Game ends\n");
			String msg = "";
			for ( int i = 0; i < 4; i++ ) {
				if ( this.playerList.get(i).getNumOfCards() == 0 ) {
					msg += String.format("Player %s wins the game.\n", this.playerList.get(i).getName());
					ui.printMsg(String.format("Player %s wins the game.\n", this.playerList.get(i).getName()));
				}
				else {
					msg += String.format("Player %s has %d cards in hand.\n", this.playerList.get(i).getName(), this.playerList.get(i).getNumOfCards());
					ui.printMsg(String.format("Player %s has %d cards in hand.\n", this.playerList.get(i).getName(), this.playerList.get(i).getNumOfCards()));
				}
			}
			for (int i = 0; i < 4; i++)
				playerList.get(i).removeAllCards();
			
			int Continue = JOptionPane.showConfirmDialog(null, msg, "Game Result", JOptionPane.YES_NO_OPTION);
			//clear table
			if ( !this.getHandsOnTable().isEmpty() ) {
				for ( int i = 0; i < this.getHandsOnTable().size(); i++ ) 
					this.getHandsOnTable().remove(0);
			}
			//whether to continue
			if (Continue == 0) {
				// yes
				// send join message
				getClient().sendMessage(new CardGameMessage(1, -1, getClient().getPlayerName()));
				// send ready message
				getClient().sendMessage(new CardGameMessage(4, -1, null));
				//playerList.get(this.getClient().getPlayerID()).removeAllCards();
				ui.repaint();
			} else {
				//not continue, quit!
				System.exit(0);
			}
			return;
		}
		
	}
	/**
	 * Check whether the game is end or not.
	 * 
	 * @return endOfGame Whether the game is end or not.
	 */
	@Override
	public boolean endOfGame() {
		for ( int i = 0; i < 4; i++ ) {
			if ( playerList.get(i).getNumOfCards() == 0 ) return true;
		}
		return false;
	}
	/**
	 * The main function.
	 * 
	 * @param args The arguments passed in.
	 */
	public static void main(String[] args) {
			BigTwo game = new BigTwo();
	}
		
	/**
	 * Output a hand given some cards.
	 * 
	 * @param player The player pass by the checkmove.
	 * @param cards The cards the player play.
	 * @return hand Valid hand or null.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		if ( cards == null ) return null;
		else if ( cards.size() < 5) {
			if ( cards.size() == 1 ) return new Single(player, cards);
			else if ( cards.size() == 2 ) {
				if ( new Pair(player, cards).isValid() ) return new Pair(player, cards);
			}
			else if ( cards.size() == 3 ) {
				if ( new Triple(player, cards).isValid() ) return new Triple(player, cards);
			}
			else return null;
		}
		else if ( cards.size() == 5 ) { //descending order
			if ( new StraightFlush(player, cards).isValid() ) return new StraightFlush(player, cards);
			else if ( new Quad(player, cards).isValid() ) return new Quad(player, cards);
			else if ( new FullHouse(player, cards).isValid() ) return new FullHouse(player, cards);
			else if ( new Flush(player, cards).isValid() ) return new Flush(player, cards);
			else if ( new Straight(player, cards).isValid() ) return new Straight(player, cards);
		}
		return null;
	}

}