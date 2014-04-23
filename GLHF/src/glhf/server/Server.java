package glhf.server;

import glhf.client.Client;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.util.Map;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.message.Message;
import crossnet.message.MessageParser;

/**
 * The Serve of the GLHF.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Server {

	/**
	 * The underlying CrossNet server.
	 */
	private final crossnet.Server crossnetServer;

	/**
	 * The GLHF MessageParser.
	 */
	private final MessageParser messageParser = new ServerMessageParser();

	/**
	 * The ConnectionHandler.
	 */
	private final ServerConnectionHandler serverConnectionHandler;

	/**
	 * Creates and starts the Server.
	 */
	public Server() {
		// Overrides the Server to use a Connection subclass.
		this.crossnetServer = new crossnet.Server() {

			@Override
			protected Connection newConnection() {
				return new GlhfConnection();
			}
		};

		// Sets the GLHF MessageParser as the tiered parser on the CrossNet parser.
		this.crossnetServer.getMessageParser().setTieredMessageParser( this.messageParser );

		// Sets the GLHF ConnectionHandler
		this.serverConnectionHandler = new ServerConnectionHandler( this );
		this.crossnetServer.addConnectionListener( this.serverConnectionHandler );

		// Starts the Server thread
		this.crossnetServer.start( "CrossNet Server" );
	}

	/**
	 * Add a ConnectionListener to the CrossNet Server.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addConnectionListener( ConnectionListener listener ) {
		this.crossnetServer.addConnectionListener( listener );
	}

	/**
	 * Remove a ConnectionListener from the CrossNet Server.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeConnectionListener( ConnectionListener listener ) {
		this.crossnetServer.removeConnectionListener( listener );
	}

	/**
	 * Gets the GLHF MessageParser. Add another {@link MessageParser} to this, to get tiered parsing of custom
	 * {@link Message}s.
	 * 
	 * @return The GLHF MessageParser.
	 */
	public MessageParser getMessageParser() {
		return this.messageParser;
	}

	/**
	 * Bind the Server to a TCP port and start listening for new connections.
	 * 
	 * @param port
	 *            The TCP port on which to listen
	 * @throws IOException
	 *             If the server could not bind correctly.
	 */
	public void bind( int port ) throws IOException {
		this.crossnetServer.bind( port );
	}

	/**
	 * Stops the {@link Server} update thread.
	 */
	public void stop() {
		this.crossnetServer.stop();
	}

	/**
	 * Gets the current mapping of IDs to {@link Connection}s.
	 * <p>
	 * NB: Do not modify this.
	 * 
	 * @return The current mapping of IDs to {@link Connection}s.
	 */
	Map< Integer, Connection > getConnections() {
		return this.crossnetServer.getConnections();
	}

	/**
	 * Send a Message to all connected {@link Client}s.
	 * 
	 * @param message
	 *            The Message to send.
	 */
	void sendToAll( Message message ) {
		this.crossnetServer.sendToAll( message );
	}

	/**
	 * Send a Message to all connected {@link Client}s, except for the one with ID.
	 * 
	 * @param id
	 *            The ID to skip.
	 * @param message
	 *            The Message to send.
	 */
	void sendToAllExcept( int id, Message message ) {
		this.crossnetServer.sendToAllExcept( id, message );
	}

	/**
	 * Add a PlayerListener to the ConnectionHandler.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addPlayerListener( PlayerListener listener ) {
		this.serverConnectionHandler.addListener( listener );
	}

	/**
	 * Remove a PlayerListener from the ConnectionHandler.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removePlayerListener( PlayerListener listener ) {
		this.serverConnectionHandler.removeListener( listener );
	}

	/**
	 * Gets the current mapping of IDs to {@link Player}s.
	 * <p>
	 * NB: Do not modify this.
	 * 
	 * @return The current mapping of IDs to {@link Player}s.
	 */
	public Map< Integer, Player > getPlayers() {
		return this.serverConnectionHandler.getPlayers();
	}

}
