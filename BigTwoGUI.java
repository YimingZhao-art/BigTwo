import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
/**
 * This is a sub-class of CardGame. It implements the "Big Two" game.
 *
 * @author yimingzhao
 * @version 3
 */
public class BigTwoGUI implements CardGameUI {
	
	private BigTwo game; //The BigTwoGame
	private boolean[] selected = new boolean[13]; //Whether the cards are selected.
	private int activePlayer = -1; //Active Player idx
	private JFrame frame; //GUI frame
	private JPanel bigTwoPanel; //A panel containing players' cards and cards on table.
	private JButton playButton; //Play the hand.
	private JButton passButton; //Pass the turn.
	private JTextArea msgArea; //Show the game message.
	private JTextArea chatArea; //Showing chat messages.
	private JTextField chatInput; //Text field to send the message.
	
	
	//added variables
	private ArrayList<CardGamePlayer> playerList; //player list
	private ArrayList<Hand> handsOnTable; //hands on table
	private boolean cardEnable = true; //Whether cards of activePlayer can be clicked.
	private boolean[] exist = new boolean[4]; //whether the player is in the gui

	/**
	 * The constructor of the GUI, it will set all the parts except the BigTwoPanel, to refresh the GUI, BigTwoPanel will created in the repaint().
	 *
	 * @param game The Big Two game.
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game; //initial
		this.playerList = game.getPlayerList(); //initial
		this.handsOnTable = game.getHandsOnTable(); //initial

		
		frame = new JFrame("Big Two");
		frame.setSize(900,720);
		frame.setLocationRelativeTo(null); //In the center of screen, this step can't before the last step, otherwise the left-top corner will be in the center.

		//Following will added to frame here instead in repaint, otherwise the screen will strobe.

		JMenuBar bar = new JMenuBar(); //whole menu bar
		JMenu menu = new JMenu("Game"); //restart or exit
		JMenuItem restart = new JMenuItem("Connect");
		JMenuItem quit = new JMenuItem("Quit");
		restart.addActionListener(new RestartMenuItemListener()); //action
		quit.addActionListener(new QuitMenuItemListener()); //action
		menu.add(restart);
		menu.add(quit);
		JMenuItem rules = new JMenuItem("Rules");
		rules.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event){
				JOptionPane.showMessageDialog(null, "1. A standard 52 card pack is used.\n"+
					"2. The order of ranks from high to low is 2, A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3.\n"+
					"3. The order of suits from high to low is Spades, Hearts, Clubs, Diamonds.\n"+
					"4. There are always four players in a game.\n"+
					"5. Each player holds 13 (randomly assigned) cards at the beginning of the game.\n"+
					"6.The player holding the Three of Diamonds will begin the game by playing a hand of\n"+
					"legal combination of cards that includes the Three of Diamonds. He/she cannot pass\n"+
					"his/her turn to the next player without making his/her move.\n"+
					"7. Players take turns to play by either playing a hand of legal combination of cards that\n"+
					"beats the last hand of cards played on the table, or by passing his/her turn to the next\n"+
					"player.\n"+
					"8. A player cannot pass his/her turn to the next player if he/she is the one who played the\n"+
					"last hand of cards on the table. In this case, he/she can play a hand of any legal\n"+
					"combination of cards regardless of the last hand he/she played on the table.\n"+
					"9. A hand of legal combination of cards can only be beaten by another better hand\n"+
					"of legal combination of cards with the same number of cards.\n"+
					"10. The game ends when any of the players has no more cards in his/her hand.\n");
			}
		});
		JMenuItem help = new JMenuItem("Help");
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "Kenneth K.Y. Wong\n"+"Tel.: 3917 8483\n"+"kywong@eee.hku.hk \n"+"Web:https://www.eee.hku.hk/~kywong\n");
			}
		});
		JMenuItem resize = new JMenuItem("Resize the window");
		resize.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				frame.setSize(900, 720);
			}
		});
		JMenuItem clear = new JMenuItem("Clear MsgArea");
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				clearMsgArea();
			}
		});
		JMenuItem clear1 = new JMenuItem("Clear ChatArea");
		clear1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				clearChatArea();
			}
		});
		
		JMenu info = new JMenu("Information"); //information menu
		info.add(rules);
		info.add(help);
		info.add(resize);
		info.add(clear);
		info.add(clear1);
		bar.add(menu);
		bar.add(info);
		frame.setJMenuBar(bar); //set instead of adding
		
		//msg area and chat area and inputfield will be in the east panel
		msgArea = new JTextArea(60, 30);
		chatArea = new JTextArea(60, 30);
		//input field with a label
		JLabel message = new JLabel("Message: ");
		chatInput = new JTextField(20);
		chatInput.addKeyListener(new KeyListener() { //click "Enter" will send
			@Override
			public void keyReleased(KeyEvent e) {
				if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
					String text = playerList.get(activePlayer).getName() +": "+ chatInput.getText()+"\n";
					/*chatArea.append(playerList.get(activePlayer).getName() +": "+ chatInput.getText()+"\n");*/
					CardGameMessage message = new CardGameMessage(7, -1, chatInput.getText());
									game.getClient().sendMessage(message);
					chatInput.setText("");
					//chatArea.setCaretPosition(chatArea.getText().length());
				}
			}
			public void keyPressed(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		JPanel east = new JPanel(); //east panel
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS)); //from top to bottom add three parts
		east.setBackground(Color.GRAY); //background
		msgArea.setEditable(false); //can't be edited
		chatArea.setEditable(false); //can't be edited
		chatArea.setForeground(Color.BLUE); //chat message is in blue
		JScrollPane scroller = new JScrollPane(msgArea);
		msgArea.setLineWrap(true); // make text to multiple rows
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		east.add(scroller); // add scroller, but not text
		
		
		JScrollPane scroller1 = new JScrollPane(chatArea);
		chatArea.setLineWrap(true); // make text to multiple rows
		scroller1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		east.add(scroller1); // add scroller, but not text

		JPanel pp = new JPanel(); //bind the Jlabel and Jtextfield
		pp.setBackground(Color.GRAY);
		pp.add(message);
		pp.add(this.chatInput);
		east.add(pp);
		frame.add(east, BorderLayout.EAST);

		//Two buttons will be added to the south
		Font font = new Font("Times New Roman", Font.BOLD, 20); //button font
		this.playButton = new JButton("Play");
		this.passButton = new JButton("Pass");
		playButton.setFont(font);
		passButton.setFont(font);
		this.playButton.addActionListener(new PlayButtonListener()); //action listener
		this.passButton.addActionListener(new PassButtonListener()); //action listener
		playButton.setForeground(new Color(235, 150, 40)); //the foreground is the font color
		passButton.setForeground(new Color(235, 150, 40)); //the foreground is the font color
		playButton.setPreferredSize(new Dimension(60,40)); //adjust the size
		passButton.setPreferredSize(new Dimension(60,40)); //adjust the size
		JPanel twoButton = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -4379163288056052436L;

			/**
			 * Override the paint
			 * 
			 * @param g painters
			 */
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage((new ImageIcon("imgs/cards/Advertisement.png")).getImage(), 0, 0, frame.getWidth(), 120, this);
				//add a background image
			}
		};
		twoButton.add(this.playButton);
		twoButton.add(this.passButton);
		
		frame.add(twoButton, BorderLayout.SOUTH);
		
		repaint(); // need to be repaint(), otherwise the bigtwopanel will not be added.
	}

	/**
	 * Set the activePlayer of GUI.
	 *
	 * @param activePlayer ActivePlayer index, game end will be -1.
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * Repaint the GUI, create the bigTwoPanel, will update the cards location. When activePlayer is -1, disable() is called.
	 */
	@Override
	public void repaint() {
		//frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.bigTwoPanel = new BigTwoPanel();
		frame.add(this.bigTwoPanel, BorderLayout.WEST);
		//check whether the game is end, end will diable
		
		if ( activePlayer == -1 ) {
			disable();
		}
		else enable();
		
		if ( game.getClient() != null && activePlayer != game.getClient().getPlayerID()) {
			playButton.setEnabled(false);
			passButton.setEnabled(false);
			
		}
		

	}

	
	/**
	 * Set the existence of certain player
	 * 
	 * @param index ID of player to set.
	 * @param in Whether he is in or not.
	 */
	public void setExist(int index, boolean in) {
		if ( in ) exist[index] = true;
		else exist[index] = false;
	}
	
	
	
	
	/**
	 * Append the msg into the msgArea. When append, need add newLine manually.
	 *
	 * @param msg String to be append directly.
	 */
	@Override
	public void printMsg(String msg) {
		this.msgArea.append(msg);
		msgArea.setCaretPosition(this.msgArea.getText().length()); // cursor follow the last line
		
	}

	/**
	 * Print msg to chatArea
	 * 
	 * @param msg Msg to print.
	 */
	public void printChat(String msg) {
		this.chatArea.append(msg + "\n");
		chatArea.setCaretPosition(this.chatArea.getText().length()); // cursor follow the last line
	}
	
	/**
	 * Clear the msgArea by setting text to ""
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}
	/**
	 * Clear the ChatArea by setting text to ""
	 */
	public void clearChatArea() {
		chatArea.setText("");
	}
	
	
	/**
	 * Reset the variables in GUI
	 */
	@Override
	public void reset() {
		for ( int i = 0; i < 13; i++ ) {
			selected[i] = false;
		}
		//this.game = game;
		this.playerList = game.getPlayerList();
		this.handsOnTable = game.getHandsOnTable();
		cardEnable = true; //card can be click
		clearMsgArea();
		clearChatArea();
		//Interaction
	}

	/**
	 * Make the button, chatinput, cards are able to react. Actually, this part can be used in the BigTwo.
	 * However, since I repaint() after each move, when game end, it's OK to call the function in the repaint().
	 */
	@Override
	public void enable() {
		//button clickable
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		//enable chatinput
		chatInput.setEnabled(true);
		//BigTwoPanel selection
		cardEnable = true;
	}

	/**
	 * Make the button, chatinput, cards are unable to react. Actually, this part can be used in the BigTwo.
	 * However, since I repaint() after each move, when game end, it's OK to call the function in the repaint().
	 */
	@Override
	public void disable() {
		//disable clickable
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		//disable chat
		chatInput.setEnabled(false);
		//disable choose card
		cardEnable = false;
	}

	/**
	 * Different from non-GUI, GUI only need to get the information when click button, so here only need to print msg.
	 */
	@Override
	public void promptActivePlayer() {
		printMsg(playerList.get(activePlayer).getName() + "'s turn: ");
		
	}

	//Return the ImageIcon of specific card.
	private ImageIcon cardFace(Card card, boolean face) {
		if ( !face ) {
			return new ImageIcon("imgs/cards/b.gif");
		}
		else {
			char[] suit = {'d', 'c', 'h', 's'};
			char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
			return new ImageIcon("imgs/cards/" + rank[card.getRank()] + suit[card.getSuit()]+".gif");
		}
	}

	//Return the ImageIcon of player.
	private ImageIcon getAvatar(int playerID) {
		if ( playerID > -1 && playerID < 4 ) 
			return new ImageIcon("imgs/avatars/"+ (playerID+1) +".jpeg");
		else return null;
	}

	//Get selected index from selected[]
	private int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}
		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}

	//Reset the selected[]
	private void resetSelected() {
		for (int j = 0; j < selected.length; j++) {
			selected[j] = false;
		}
	}

	//This is a class inheritting from the JPanel
	class BigTwoPanel extends JPanel {
		private static final long serialVersionUID = 2315854436994248827L; //serialID

		public BigTwoPanel() {
			setLayout(null); //If not, the cardLabel can't be added by location.
			setPreferredSize(new Dimension(520,600)); //Set the size of Panel
		}

		class CardLabel extends JLabel implements MouseListener {
			private static final long serialVersionUID = 2225524684721231075L;
			public int idOfPlayer; //Which player the card is belong to.
			public int idOfCard; //Which place is the card in.
			public int x; //The card x-coordinate in the BigTwoPanel.
			public int y; //The card y-coordinate in the BigTwoPanel.
			public int width = 73; //The width of card.
			public int height = 97; //The height of card.

			//Construct the CardLabel with icon, id of player and id of card.
			public CardLabel(ImageIcon icon,int idOfPlayer, int idOfCard) {
				super(icon); //construct use super(ImageIcon)
				//Set the coordinates
				x = 120 + idOfCard * 15;
				if ( idOfPlayer == game.getClient().getPlayerID() && selected[idOfCard] ) { //seleced of active will be a little high.
					y = idOfPlayer * 120;
				}
				else {
					y = idOfPlayer * 120 + 20;
				}
				this.idOfPlayer = idOfPlayer;
				this.idOfCard = idOfCard;

			}

			//Override the paintComponet of JLabel. Paint the card
			@Override
			public void paintComponent(Graphics g) {
				removeAll(); //clear the panel
				super.paintComponent(g);
				x = 120 + idOfCard * 15;
				if ( idOfPlayer == game.getClient().getPlayerID() && selected[idOfCard] ) {
					y = idOfPlayer * 120;
				}
				else {
					y = idOfPlayer * 120 + 20;
				}
				//When enabel, add the mouseListener
				if ( cardEnable ) {
					this.addMouseListener(this); 
				}
			}

			//Check whether the card is selected and belongs to activePlayer.
			@Override
			public void mouseReleased(MouseEvent e) {
				if ( idOfPlayer == game.getClient().getPlayerID() ) { //only card of local can react
					selected[idOfCard] = !selected[idOfCard]; //reset the selected[idx] value
					frame.repaint(); //refresh
				}
			}
			//Not implement methods are as follows
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		}

		//Override the paintComponet of the JPanel.
		@Override
		public void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			super.removeAll(); //clear the panel.
			//set the background
			Image table = Toolkit.getDefaultToolkit().getImage("imgs/cards/table.jpeg");
			g.drawImage(table,0,0,520,620,this);
			
			CardLabel card = null; //this is card to be added on player panel
			JLabel cardToAdd = null; //this is card to be added on table
			//Add the player's card
			for ( int i = 0; i < 4; i ++ ) {
				if ( exist[i] ) {
					JLabel avatar = new JLabel(getAvatar(i)); //set Icon
					avatar.setHorizontalTextPosition(JLabel.CENTER);
					avatar.setVerticalTextPosition(JLabel.TOP);//Text will be above the image
					avatar.setBounds(10, 0+i*120, 97, 117);
					if ( i == game.getClient().getPlayerID() ) {
						avatar.setForeground(Color.RED);
						avatar.setText("You"); //set the index according to status
					}
					else {
						avatar.setForeground(Color.BLUE);
						avatar.setText(playerList.get(i).getName());
					}
					this.add(avatar);
					
					if ( i == activePlayer ) {
						ImageIcon image = new ImageIcon("imgs/avatars/flag.png");
						image.setImage(image.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT));
						JLabel flag = new JLabel(image);
						flag.setBounds(390, 10+i*120, 120, 120);
						this.add(flag);
						
					}
					CardList cards = playerList.get(i).getCardsInHand();
					
					ArrayList<CardLabel> cardsToAdd = new ArrayList<>();
					boolean showFace = i == game.getClient().getPlayerID() || activePlayer == -1;
					for ( int j = 0; j < cards.size(); j++ ) {
						card = new CardLabel(cardFace(cards.getCard(j), showFace),i, j);
						card.setBounds(card.x, card.y, card.width, card.height);
						cardsToAdd.add(card);
					}
					for ( int j = cardsToAdd.size()-1; j > -1; j-- ) {
						this.add(cardsToAdd.get(j)); //cards need to be added from the tail to get the right is on the top of left.
					}
				}
				else {
					JLabel avatar = new JLabel(); //set Icon
					avatar.setHorizontalTextPosition(JLabel.CENTER);
					avatar.setVerticalTextPosition(JLabel.TOP);//Text will be above the image
					avatar.setBounds(10, 0+i*120, 97, 117);
					avatar.setForeground(Color.black);
					avatar.setText("No player");
					this.add(avatar);
				}
				g.drawLine(0, 120*(i+1), 520, 120*(i+1)); //Section line between different parts.
			}
			//Add the hand
			Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
			if ( lastHandOnTable == null ) {
				g.drawString("No hand on table", 10, 495);
			}
			else {
				g.drawString("Played by " + lastHandOnTable.getPlayer().getName(), 10, 495);
				ArrayList<JLabel> cardList = new ArrayList<>();
				if ( handsOnTable.size() > 1 ) {
					cardToAdd = new JLabel(cardFace(lastHandOnTable.getCard(0), false));
					cardToAdd.setBounds(10, 500, 73, 97);
					this.add(cardToAdd);
				}
				for ( int i = 0; i < lastHandOnTable.size(); i++ ) {
					cardToAdd = new JLabel(cardFace(lastHandOnTable.getCard(i), true));
					cardToAdd.setBounds(93+i*15, 500, 73, 97);
					cardList.add(cardToAdd);
				}
				for ( int i = cardList.size()-1; i > -1; i-- ) {
					this.add(cardList.get(i));
				}
			}
		}
	}


	class PlayButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if ( game.getClient().getPlayerID() == activePlayer ) {
				var x = getSelected(); //store the getselected
				
				if ( x != null ) { //pass can't be made by playButton
					game.makeMove(activePlayer, x);
					resetSelected(); //reset
					repaint(); //refresh
				}
			}
			else {
				printMsg("\n");
				printMsg("It is not your turn\n");
				promptActivePlayer();
				resetSelected();
				repaint();
			}
		}
	}

	class PassButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if ( activePlayer == game.getClient().getPlayerID() ) {
				game.makeMove(activePlayer, null); //only pass the null
				resetSelected();
				repaint(); //refresh 
			}
			else {
				printMsg("\n");
				printMsg("It is not your turn\n");
				promptActivePlayer();
				resetSelected();
				repaint();
			}
		}
	}

	class RestartMenuItemListener implements ActionListener {
		@Override
			public void actionPerformed(ActionEvent e) {
				if (game.getClient().getPlayerID() == -1) {
					game.getClient().connect();
				} else if (game.getClient().getPlayerID() >= 0 && game.getClient().getPlayerID() <= 3)
					printMsg("You are already connected to the server!\n");
			}
	}
	
	class QuitMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			printMsg("Game Ended by the User!\n");
			System.exit(0);
		}
	}


}
