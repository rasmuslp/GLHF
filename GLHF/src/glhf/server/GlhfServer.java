package glhf.server;

import glhf.client.GlhfClient;
import glhf.common.message.GlhfMessageParser;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.util.Map;

import crossnet.Connection;
import crossnet.CrossNetServer;
import crossnet.listener.ConnectionListener;
import crossnet.message.Message;
import crossnet.message.MessageParser;

/**
 * The Serve of the GLHF.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class GlhfServer {

	/**
	 * The underlying CrossNet server.
	 */
	private final CrossNetServer crossNetServer;

	/**
	 * The GLHF MessageParser.
	 */
	private final MessageParser messageParser = new GlhfMessageParser();

	/**
	 * The ConnectionHandler.
	 */
	private final ServerConnectionHandler serverConnectionHandler;

	/**
	 * Creates and starts the GlhfServer.
	 */
	public GlhfServer() {
		// Overrides the Server to use a Connection subclass and piggyback on the update loop.
		this.crossNetServer = new CrossNetServer() {

			/**
			 * Timestamp for last run of update piggyback.
			 */
			private long updateTimestamp = 0;

			/**
			 * Maximum time in milliseconds between calls for the update piggyback.
			 */
			private int updateMillis = 1000;

			/**
			 * Sets the maximum time between calls for the update piggyback.
			 * This can run no faster than the timeout of the CrossNet servers update.
			 * 
			 * @param updateMillis
			 *            Set to -1 to disable.
			 */
			public void setUpdate( int updateMillis ) {
				this.updateMillis = updateMillis;
			}

			@Override
			public void update( int timeout ) throws IOException {
				super.update( timeout );

				if ( this.updateMillis != -1 ) {
					long time = System.currentTimeMillis();
					if ( ( this.updateTimestamp + this.updateMillis ) <= time ) {
						this.updateTimestamp = time;
						GlhfServer.this.updatePiggyBack();
					}
				}

			}
		};

		// Sets the GLHF MessageParser as the tiered parser on the CrossNet parser.
		this.crossNetServer.getMessageParser().setTieredMessageParser( this.messageParser );

		// Sets the GLHF ConnectionHandler
		this.serverConnectionHandler = new ServerConnectionHandler( this );
		this.crossNetServer.addConnectionListener( this.serverConnectionHandler );

		// Starts the Server thread
		this.crossNetServer.start( "CrossNet Server" );
	}

	/**
	 * Add a ConnectionListener to the CrossNet Server.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addConnectionListener( ConnectionListener listener ) {
		this.crossNetServer.addConnectionListener( listener );
	}

	/**
	 * Remove a ConnectionListener from the CrossNet Server.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeConnectionListener( ConnectionListener listener ) {
		this.crossNetServer.removeConnectionListener( listener );
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
		this.crossNetServer.bind( port );
	}

	/**
	 * Stops the {@link GlhfServer} update thread.
	 */
	public void stop() {
		this.crossNetServer.stop();
	}

	/**
	 * Gets the current mapping of IDs to {@link Connection}s.
	 * <p>
	 * NB: Do not modify this.
	 * 
	 * @return The current mapping of IDs to {@link Connection}s.
	 */
	public Map< Integer, Connection > getConnections() {
		return this.crossNetServer.getConnections();
	}

	/**
	 * Send a Message to all connected {@link GlhfClient}s.
	 * 
	 * @param message
	 *            The Message to send.
	 */
	public void sendToAll( Message message ) {
		this.crossNetServer.sendToAll( message );
	}

	/**
	 * Send a Message to all connected {@link GlhfClient}s, except for the one with ID.
	 * 
	 * @param id
	 *            The ID to skip.
	 * @param message
	 *            The Message to send.
	 */
	public void sendToAllExcept( int id, Message message ) {
		this.crossNetServer.sendToAllExcept( id, message );
	}

	/**
	 * Piggy back on the CrossNet Server's update loop.
	 */
	void updatePiggyBack() {
		this.serverConnectionHandler.updatePings();
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
