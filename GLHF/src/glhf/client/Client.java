package glhf.client;

import glhf.common.User;
import glhf.message.client.SetNameMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import crossnet.message.MessageParser;

public class Client {

	private final crossnet.Client crossnetClient = new crossnet.Client();

	private final MessageParser messageParser = new ClientMessageParser();

	private final Map< Integer, User > users = new HashMap<>();

	private final ClientListener clientListener;

	public Client() {
		this.crossnetClient.getMessageParser().setTieredMessageParser( this.messageParser );

		this.clientListener = new ClientListener( this );
		this.crossnetClient.addConnectionListener( this.clientListener );

		this.crossnetClient.start( "CrossNet Client" );
	}

	public void connect( InetAddress host, int port ) throws IOException {
		this.crossnetClient.connect( host, port, 5000 );
	}

	/**
	 * Gets the ID of this. Will be -1 if not properly connected to {@link Server}.
	 * 
	 * @return The ID of this.
	 */
	public int getID() {
		return this.crossnetClient.getConnection().getID();
	}

	public Map< Integer, User > getUsers() {
		// When connected, will include all users; i.e. also this Client, not just other users.
		return this.users;
	}

	public void setName( String name ) {
		SetNameMessage setNameMessage = new SetNameMessage( name );
		this.crossnetClient.getConnection().send( setNameMessage );
	}

}
