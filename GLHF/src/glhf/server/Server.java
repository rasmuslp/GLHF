package glhf.server;

import glhf.message.IdTuple;
import glhf.message.client.SetNameMessage;
import glhf.message.common.ChatMessage;
import glhf.message.server.NamesMessage;

import java.util.ArrayList;
import java.util.List;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Server extends crossnet.Server {

	private MessageParser messageParser = new ServerMessageParser();

	public Server() {
		super.messageParser.setTieredMessageParser( this.messageParser );

		ConnectionListener connectionListener = new ConnectionListenerAdapter() {

			@Override
			public void connected( Connection connection ) {
				Message message = new ChatMessage( -1, -1, "Hello and die !" );
				connection.send( message );
			}

			@Override
			public void received( Connection connection, Message message ) {
				if ( message instanceof SetNameMessage ) {
					SetNameMessage setNameMessage = (SetNameMessage) message;
					//TODO: Store names locally for when a new client connects, this will need to know the names of all the allready connected clients.
					IdTuple< String > name = new IdTuple<>( connection.getID(), setNameMessage.getName() );
					List< IdTuple< String > > names = new ArrayList<>();
					names.add( name );
					NamesMessage namesMessage = new NamesMessage( names );
					Server.this.sendToAll( namesMessage );
				}
			}
		};

		this.addConnectionListener( connectionListener );
	}
}
