package glhf.server;

import glhf.message.IdTuple;
import glhf.message.client.SetNameMessage;
import glhf.message.common.ChatMessage;
import glhf.message.server.NamesMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Server {

	private crossnet.Server crossnetServer = new crossnet.Server();

	private MessageParser messageParser = new ServerMessageParser();

	public Server() {
		this.crossnetServer.getMessageParser().setTieredMessageParser( this.messageParser );

		ServerListener serverListener = new ServerListener();
		this.crossnetServer.addConnectionListener( serverListener );

		this.crossnetServer.start( "CrossNet Server" );
	}

	public void bind( int port ) throws IOException {
		this.crossnetServer.bind( port );
	}

	public class ServerListener extends ConnectionListenerAdapter {

		@Override
		public void connected( Connection connection ) {
			System.out.println( connection + " connected." );

			Message message = new ChatMessage( -1, -1, "Hello and die !" );
			connection.send( message );
		}

		@Override
		public void disconnected( Connection connection ) {
			System.out.println( connection + " disconnected." );
		}

		@Override
		public void received( Connection connection, Message message ) {
			System.out.println( connection + " received: " + message.getClass().getSimpleName() );
			if ( message instanceof SetNameMessage ) {
				SetNameMessage setNameMessage = (SetNameMessage) message;
				//TODO: Store names locally for when a new client connects, this will need to know the names of all the allready connected clients.
				IdTuple< String > name = new IdTuple<>( connection.getID(), setNameMessage.getName() );
				List< IdTuple< String > > names = new ArrayList<>();
				names.add( name );
				NamesMessage namesMessage = new NamesMessage( names );
				Server.this.crossnetServer.sendToAll( namesMessage );
			}
		}

		@Override
		public void idle( Connection connection ) {
			// Override this if necessary.
		}

	}
}
