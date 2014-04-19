package glhf.server;

import glhf.common.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crossnet.Connection;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Server {

	private final crossnet.Server crossnetServer = new crossnet.Server();

	private final MessageParser messageParser = new ServerMessageParser();

	private final Map< Integer, Player > players = new HashMap<>();

	private final ServerListener serverListener;

	public Server() {
		this.crossnetServer.getMessageParser().setTieredMessageParser( this.messageParser );

		this.serverListener = new ServerListener( this );
		this.crossnetServer.addConnectionListener( this.serverListener );

		this.crossnetServer.start( "CrossNet Server" );
	}

	public void bind( int port ) throws IOException {
		this.crossnetServer.bind( port );
	}

	public Map< Integer, Player > getPlayers() {
		return this.players;
	}

	List< Connection > getConnections() {
		return this.crossnetServer.getConnections();
	}

	void sendToAll( Message message ) {
		this.crossnetServer.sendToAll( message );
	}

	void sendToAllExcept( int id, Message message ) {
		this.crossnetServer.sendToAllExcept( id, message );
	}

}
