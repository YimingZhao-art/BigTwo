
import java.net.*;
import java.io.*;
import javax.swing.*;
/**
 * This is a bigtwoclient class
 * 
 * @author yimingzhao
 * @version 1
 */
public class BigTwoClient implements NetworkGame {
	
	private BigTwo game; //The BigTwo game
	private BigTwoGUI gui; //The gui
	private Socket sock; //The socket
	private ObjectOutputStream oos; //output to the server
	private int playerID; //id of the local
	private String playerName; //local playername
	private String serverIP; //server IP
	private int serverPort; //server Port
	
	/**
	 * Constructor of client with game and gui.
	 * 
	 * @param game BigTwo Game.
	 * @param gui BigTwo GUI
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game = game;
		this.gui = gui;
		
		gui.disable(); //only enable when the game start
		playerName = (String) JOptionPane.showInputDialog("Enter your name: ");
		if (playerName == null || playerName.trim().isEmpty() == true)
			playerName = "No Name";
		
		
		
		
		
		this.serverIP = "localhost"; //set the serverIP
		this.serverPort = 2396; //set the server
		connect();
		gui.repaint();
		
	}
	/**
	 * Return the localID.
	 * 
	 * @return LocalID
	 */
	public int getPlayerID() {
		return this.playerID;
	}
	/**
	 * Return the localNAME.
	 * 
	 * @return localName
	 */
	public String getPlayerName() {
		return this.playerName;
	}
	/**
	 * Return the serverIP.
	 * 
	 * @return serverIP
	 */
	public String getServerIP() {
		return this.serverIP;
	}
	/**
	 * Return the serverPort.
	 * 
	 * @return serverPort
	 */
	public int getServerPort() {
		return this.serverPort;
	}
	/**
	 * Set the playerID
	 * 
	 * @param playerID The local ID.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	/**
	 * Set the playerName.
	 * 
	 * @param playerName The local name.
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * Set the serverIP.
	 * 
	 * @param serverIP ServerIP.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	/**
	 * Set the serverPort.
	 * 
	 * @param serverPort serverPort.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	/**
	 * Try to connect to the server and output to the server.
	 */
	public void connect() {
		try {
			sock = new Socket(this.serverIP, this.serverPort);
			//new thread to receive
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread receive = new Thread(new ServerHandler());
		receive.start();
		//Successfully connect will send message.
		sendMessage(new CardGameMessage(1, -1, this.getPlayerName())); //join
		sendMessage(new CardGameMessage(4, -1, null)); //ready locally
		gui.repaint();
		
	}
	/**
	 * Parse the message from the server.
	 * 
	 * @param message Message from the server.
	 */
	public void parseMessage(GameMessage message) {
		switch (message.getType()) {
			case CardGameMessage.PLAYER_LIST:
				//set the player
				this.setPlayerID(message.getPlayerID());
				for ( int i = 0; i < 4; i++ ) {
					if (((String[])message.getData())[i] != null) {
						game.getPlayerList().get(i).setName(((String[])message.getData())[i]);
						gui.setExist(i, true); //if there, update the gui
					}
				}
				gui.repaint();
				break;
			case CardGameMessage.JOIN:
				game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
				gui.setExist(message.getPlayerID(), true);
				gui.repaint();
				gui.printMsg("Player " + game.getPlayerList().get(message.getPlayerID()).getName() + " joined the game!\n");
				//join id null
				break;
			case CardGameMessage.FULL:
				//full -1 null
				this.setPlayerID(-1); //This help to reconnect.
				gui.printMsg("Full now!Try next time!\n");
				gui.setActivePlayer(-1); //This will disable the full gui.
				gui.repaint();
				break;
			case CardGameMessage.QUIT:
				//quit id ip/port
				gui.printMsg("Player " + message.getPlayerID() + " " + game.getPlayerList().get(message.getPlayerID()).getName() + " left the game.\n");
				game.getPlayerList().get(message.getPlayerID()).setName("");
				/*clear the table
				if ( !game.getHandsOnTable().isEmpty() ) {
					for ( int i = 0; i < game.getHandsOnTable().size(); i++ ) 
						game.getHandsOnTable().remove(0);
				}
				*/
				gui.setExist(message.getPlayerID(), false); //make the player lost
				if ( !game.endOfGame() ) {
					gui.disable();
					this.sendMessage(new CardGameMessage(4, -1, null));
					gui.repaint();
				}
				gui.setActivePlayer(-1);
				gui.repaint();
				break;
			case CardGameMessage.READY:
				gui.printMsg("Player " + game.getPlayerList().get(message.getPlayerID()).getName() + " is ready now!\n");
				game.cleanTable();
				gui.repaint();
				break;
			case CardGameMessage.START:
				//start -1 deck
				game.start((BigTwoDeck) message.getData());
				
				break;
			case CardGameMessage.MOVE:
				//move byclient id idx
				//boardcast id idx
				//every client will check the move. However, only local one will print illegal information
				game.checkMove(message.getPlayerID(), (int[])message.getData());
				game.getGUI().repaint();
				break;
			case CardGameMessage.MSG:
				//msg id name ip port msg
				gui.printChat((String)message.getData());
				break;
			default:
				gui.printMsg("Wrong message type: " + message.getType());
				gui.repaint();
				break;
		}
	}
	/**
	 * Send msg to server.
	 * 
	 * @param message Message to send.
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ServerHandler implements Runnable {
		@Override
		public void run() {
			try {
				CardGameMessage message = null;
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				while ( (message = (CardGameMessage) ois.readObject()) != null ) {
					parseMessage(message);
					//System.out.println("Receive message!"); using for debug
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			gui.repaint(); //update the gui
		}
	}
}