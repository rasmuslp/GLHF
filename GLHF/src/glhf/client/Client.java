package glhf.client;

import glhf.common.message.GlhfMessage;
import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.common.DataMessage;
import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import crossnet.listener.ConnectionListener;
import crossnet.log.Log;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Client {

	private final crossnet.Client crossnetClient = new crossnet.Client();

	private final MessageParser messageParser = new ClientMessageParser();

	private final ClientConnectionHandler clientConnectionHandler;

	public Client() {
		this.crossnetClient.getMessageParser().setTieredMessageParser( this.messageParser );

		this.clientConnectionHandler = new ClientConnectionHandler();
		this.crossnetClient.addConnectionListener( this.clientConnectionHandler );

		this.crossnetClient.start( "CrossNet Client" );
	}

	public void addConnectionListener( ConnectionListener listener ) {
		this.crossnetClient.addConnectionListener( listener );
	}

	public void removeConnectionListener( ConnectionListener listener ) {
		this.crossnetClient.removeConnectionListener( listener );
	}

	public MessageParser getMessageParser() {
		return this.messageParser;
	}

	public void connect( InetAddress host, int port ) throws IOException {
		this.crossnetClient.connect( host, port, 5000 );
	}

	public void stop() {
		this.crossnetClient.stop();
	}

	/**
	 * Gets the ID of this. Will be -1 if not properly connected to {@link Server}.
	 * 
	 * @return The ID of this.
	 */
	public int getID() {
		return this.crossnetClient.getConnection().getID();
	}

	public int send( Message message ) {
		if ( message == null ) {
			throw new IllegalArgumentException( "Cannot send null." );
		}

		String messageClass = message.getClass().getSimpleName();
		boolean wrapped = false;
		if ( !( message instanceof GlhfMessage ) ) {
			// Wrap message in DataMessage
			byte[] messageData = message.getBytes();
			message = new DataMessage( messageData );
			wrapped = true;
		}

		int length = this.crossnetClient.getConnection().send( message );
		if ( length == 0 ) {
			Log.trace( "GLHF", "Client had nothing to send." );
		} else if ( wrapped ) {
			Log.debug( "GLHF", "Client sent: " + messageClass + " (" + length + ")" );
		}
		return length;
	}

	public void addPlayerListener( PlayerListener listener ) {
		this.clientConnectionHandler.addListener( listener );
	}

	public void removePlayerListener( PlayerListener listener ) {
		this.clientConnectionHandler.removeListener( listener );
	}

	public Map< Integer, Player > getPlayers() {
		// When connected, will include all players; i.e. also this Client, not just other players.
		return this.clientConnectionHandler.getPlayers();
	}

	public Player getPlayer() {
		return this.clientConnectionHandler.getPlayer();
	}

	public void chat( final String chat ) {
		this.send( new ChatMessage( this.getID(), chat ) );
	}

	public void chatPrivate( final String chat, final Player receiver ) {
		this.send( new ChatMessage( this.getID(), receiver.getID(), chat ) );
	}

	public void setName( final String name ) {
		this.send( new SetNameMessage( name ) );
	}

	public void setReady( final boolean isReady ) {
		this.send( new SetReadyMessage( isReady ) );
	}

}
