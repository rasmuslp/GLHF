package glhf.client;

import glhf.message.IdTuple;
import glhf.message.client.SetNameMessage;
import glhf.message.common.ChatMessage;
import glhf.message.server.ConnectionChangeMessage;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;

import java.io.IOException;
import java.net.InetAddress;

import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Client {

	private crossnet.Client crossnetClient = new crossnet.Client();

	private MessageParser messageParser = new ClientMessageParser();

	public Client() {
		this.crossnetClient.getMessageParser().setTieredMessageParser( this.messageParser );

		ClientListener clientListener = new ClientListener();
		this.crossnetClient.addConnectionListener( clientListener );

		this.crossnetClient.start( "CrossNet Client" );
	}

	public void connect( InetAddress host, int port ) throws IOException {
		this.crossnetClient.connect( host, port, 5000 );
	}

	public int getId() {
		return this.crossnetClient.getConnection().getID();
	}

	public void setName( String name ) {
		SetNameMessage setNameMessage = new SetNameMessage( name );
		this.crossnetClient.getConnection().send( setNameMessage );
	}

	public class ClientListener extends ConnectionListenerAdapter {

		@Override
		public void connected( Connection connection ) {
			System.out.println( connection + " connected." );
		}

		@Override
		public void disconnected( Connection connection ) {
			System.out.println( connection + " disconnected." );
		}

		@Override
		public void received( Connection connection, Message message ) {
			System.out.println( connection + " received: " + message.getClass().getSimpleName() );
			if ( message instanceof ChatMessage ) {
				ChatMessage chatMessage = (ChatMessage) message;
				System.out.println( "SCHAT: " + chatMessage.getChatMessage() );
			} else if ( message instanceof ConnectionChangeMessage ) {
				ConnectionChangeMessage connectionChangeMessage = (ConnectionChangeMessage) message;
				String out = "Connection " + connectionChangeMessage.getConnectionID();
				if ( connectionChangeMessage.didConnect() ) {
					out += " connected.";
				} else {
					out += " disconnected.";
				}
				System.out.println( out );
			} else if ( message instanceof IdsMessage ) {
				IdsMessage idsMessage = (IdsMessage) message;
				String out = "Client IDs:";
				for ( Integer i : idsMessage.getList() ) {
					out += " " + i;
				}
				System.out.println( out );
			} else if ( message instanceof NamesMessage ) {
				NamesMessage namesMessage = (NamesMessage) message;
				System.out.println( "New Client names:" );
				for ( IdTuple< String > idTuple : namesMessage.getList() ) {
					System.out.println( "Client " + idTuple.getId() + " now has the name '" + idTuple.getValue() + "'" );
				}
			}
		}

		@Override
		public void idle( Connection connection ) {
			// Override this if necessary.
		}

	}

}
