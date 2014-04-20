package glhf.test;

import glhf.client.Client;
import glhf.common.player.Player;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import crossnet.log.Log;
import crossnet.log.LogLevel;

public class StartGLHFClient {

	public static void main( String[] args ) throws UnknownHostException, IOException {
		Log.set( LogLevel.TRACE );

		Client client = new Client();
		client.connect( InetAddress.getByName( "localhost" ), 55100 );

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
				System.out.println( "-----" );
				Map< Integer, Player > players = client.getPlayers();
				for ( Player player : players.values() ) {
					System.out.println( "Player id: " + player.getID() );
				}
			} catch ( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
