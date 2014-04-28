package glhf.client;

import glhf.common.message.GlhfListMessage;
import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageParser;
import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.common.TieredGlhfMessage;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;
import glhf.server.GlhfServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import crossnet.CrossNetClient;
import crossnet.listener.ConnectionListener;
import crossnet.log.Log;
import crossnet.message.Message;
import crossnet.message.MessageParser;

/**
 * The Client of the GLHF.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class GlhfClient {

	/**
	 * The underlying CrossNetClient.
	 */
	private final CrossNetClient crossNetClient = new CrossNetClient();

	/**
	 * The GLHF MessageParser.
	 */
	private final MessageParser messageParser = new GlhfMessageParser();

	/**
	 * The ConnectionHandler.
	 */
	private final ClientConnectionHandler clientConnectionHandler;

	/**
	 * Creates and starts the Client.
	 */
	public GlhfClient() {
		// Sets the GLHF MessageParser as the tiered parser on the CrossNet parser.
		this.crossNetClient.getMessageParser().setTieredMessageParser( this.messageParser );

		// Sets the GLHF ConnectionHandler
		this.clientConnectionHandler = new ClientConnectionHandler();
		this.crossNetClient.addConnectionListener( this.clientConnectionHandler );

		// Starts the Client thread
		this.crossNetClient.start( "CrossNet Client" );
	}

	/**
	 * Add a ConnectionListener to the CrossNet Client.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addConnectionListener( ConnectionListener listener ) {
		this.crossNetClient.addConnectionListener( listener );
	}

	/**
	 * Remove a ConnectionListener from the CrossNet Client.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeConnectionListener( ConnectionListener listener ) {
		this.crossNetClient.removeConnectionListener( listener );
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
	 * Connect to a {@link GlhfServer}.
	 * 
	 * @param host
	 *            The address.
	 * @param port
	 *            The port.
	 * @throws IOException
	 *             If the connection could not be opened or the attempt timed out.
	 */
	public void connect( InetAddress host, int port ) throws IOException {
		this.crossNetClient.connect( host, port, 5000 );
	}

	/**
	 * Stops the {@link GlhfClient} update thread.
	 */
	public void stop() {
		this.crossNetClient.stop();
	}

	/**
	 * Gets the ID of this. Will be -1 if not properly connected to {@link GlhfServer}.
	 * 
	 * @return The ID of this.
	 */
	public int getID() {
		return this.crossNetClient.getConnection().getID();
	}

	/**
	 * Sends a Message to the {@link GlhfServer}.
	 * <p>
	 * If this is not a {@link GlhfMessage}, it will be wrapped in a {@link TieredGlhfMessage} for transportation.
	 * 
	 * @param message
	 *            The Message to send.
	 * @return he number of bytes added to the send buffer.
	 */
	public int send( Message message ) {
		if ( message == null ) {
			throw new IllegalArgumentException( "Cannot send null." );
		}

		String messageClass = message.getClass().getSimpleName();
		boolean wrapped = false;
		if ( !( message instanceof GlhfMessage || message instanceof GlhfListMessage ) ) {
			// Wrap message in TieredGlhfMessage
			byte[] messageData = message.getBytes();
			message = new TieredGlhfMessage( messageData );
			wrapped = true;
		}

		int length = this.crossNetClient.getConnection().send( message );
		if ( length == 0 ) {
			Log.trace( "GLHF", "Client had nothing to send." );
		} else if ( wrapped ) {
			Log.debug( "GLHF", "Client sent: " + messageClass + " (" + length + ")" );
		}
		return length;
	}

	/**
	 * Add a PlayerListener to the ConnectionHandler.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addPlayerListener( PlayerListener listener ) {
		this.clientConnectionHandler.addListener( listener );
	}

	/**
	 * Remove a PlayerListener from the ConnectionHandler.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removePlayerListener( PlayerListener listener ) {
		this.clientConnectionHandler.removeListener( listener );
	}

	/**
	 * Gets the current mapping of IDs to {@link Player}s. Note that this include _all_ Players; i.e. also the one
	 * represented by this {@link GlhfClient}.
	 * <p>
	 * NB: Do not modify this.
	 * 
	 * @return The current mapping of IDs to {@link Player}s.
	 */
	public Map< Integer, Player > getPlayers() {
		return this.clientConnectionHandler.getPlayers();
	}

	/**
	 * NB: This will be {@code null} if not connected to a {@link GlhfServer}.
	 * 
	 * @return The Player that this {@link GlhfClient} constitutes.
	 */
	public Player getPlayer() {
		return this.clientConnectionHandler.getPlayer();
	}

	/**
	 * Send a public chat message.
	 * 
	 * @param chat
	 *            The message.
	 */
	public void chat( final String chat ) {
		this.send( new ChatMessage( chat ) );
	}

	/**
	 * Send a private chat message.
	 * 
	 * @param chat
	 *            The message.
	 * @param receiver
	 *            The receiver.
	 */
	public void chatPrivate( final String chat, final Player receiver ) {
		this.send( new ChatMessage( chat, receiver.getID() ) );
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            The new name to set.
	 */
	public void setName( final String name ) {
		this.send( new SetNameMessage( name ) );
	}

	/**
	 * Sets the ready status.
	 * 
	 * @param isReady
	 *            The new ready status.
	 */
	public void setReady( final boolean isReady ) {
		this.send( new SetReadyMessage( isReady ) );
	}

}
