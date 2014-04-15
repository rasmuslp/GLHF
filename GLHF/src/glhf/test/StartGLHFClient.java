package glhf.test;

import glhf.client.Client;
import glhf.message.common.ChatMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.log.Log;
import crossnet.log.LogLevel;
import crossnet.message.Message;
import crossnet.test.DefaultListener;

public class StartGLHFClient {

	public static void main( String[] args ) throws UnknownHostException, IOException {
		Log.set( LogLevel.TRACE );

		ConnectionListener connectionListener = new DefaultListener() {

			@Override
			public void received( Connection connection, Message message ) {
				super.received( connection, message );
				if ( message instanceof ChatMessage ) {
					ChatMessage chatMessage = (ChatMessage) message;
					System.out.println( "SCHAT: " + chatMessage.getChatMessage() );
				}
			}
		};

		Client client = new Client();
		client.addConnectionListener( connectionListener );
		client.start( "Client" );
		client.connect( InetAddress.getByName( "hs.rlponline.dk" ), 55100, 5000 );

		try {
			Thread.sleep( 200 );
		} catch ( InterruptedException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		client.setName( "Cool burger" );

		while ( true ) {
			try {
				Thread.sleep( 1000 );
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
