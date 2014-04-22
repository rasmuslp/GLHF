package glhf.server;

import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

import java.io.IOException;
import java.util.Map;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Server {

	private final crossnet.Server crossnetServer;

	private final MessageParser messageParser = new ServerMessageParser();

	private final ServerConnectionHandler serverConnectionHandler;

	public Server() {
		this.crossnetServer = new crossnet.Server() {

			@Override
			protected Connection newConnection() {
				return new GlhfConnection();
			}
		};

		this.crossnetServer.getMessageParser().setTieredMessageParser( this.messageParser );

		this.serverConnectionHandler = new ServerConnectionHandler( this );
		this.crossnetServer.addConnectionListener( this.serverConnectionHandler );

		this.crossnetServer.start( "CrossNet Server" );
	}

	public void addConnectionListener( ConnectionListener listener ) {
		this.crossnetServer.addConnectionListener( listener );
	}

	public void removeConnectionListener( ConnectionListener listener ) {
		this.crossnetServer.removeConnectionListener( listener );
	}

	public MessageParser getMessageParser() {
		return this.messageParser;
	}

	public void bind( int port ) throws IOException {
		this.crossnetServer.bind( port );
	}

	public void stop() {
		this.crossnetServer.stop();
	}

	Map< Integer, Connection > getConnections() {
		return this.crossnetServer.getConnections();
	}

	void sendToAll( Message message ) {
		this.crossnetServer.sendToAll( message );
	}

	void sendToAllExcept( int id, Message message ) {
		this.crossnetServer.sendToAllExcept( id, message );
	}

	public void addPlayerListener( PlayerListener listener ) {
		this.serverConnectionHandler.addListener( listener );
	}

	public void removePlayerListener( PlayerListener listener ) {
		this.serverConnectionHandler.removeListener( listener );
	}

	public Map< Integer, Player > getPlayers() {
		return this.serverConnectionHandler.getPlayers();
	}

}
