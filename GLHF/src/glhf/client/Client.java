package glhf.client;

import glhf.message.IdTuple;
import glhf.message.client.SetNameMessage;
import glhf.message.server.NamesMessage;
import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.message.Message;
import crossnet.message.MessageParser;

public class Client extends crossnet.Client {

	private MessageParser messageParser = new ClientMessageParser();

	public Client() {
		super.messageParser.setTieredMessageParser( this.messageParser );

		ConnectionListener connectionListener = new ConnectionListenerAdapter() {

			@Override
			public void received( Connection connection, Message message ) {
				if ( message instanceof NamesMessage ) {
					NamesMessage namesMessage = (NamesMessage) message;
					System.out.println( "New Client names:" );
					for ( IdTuple< String > idTuple : namesMessage.getList() ) {
						System.out.println( "Client " + idTuple.getId() + " now has the name '" + idTuple.getValue() + "'" );
					}
				}
			}
		};

		this.addConnectionListener( connectionListener );
	}

	public void setName( String name ) {
		SetNameMessage setNameMessage = new SetNameMessage( name );
		this.getConnection().send( setNameMessage );
	}

}
