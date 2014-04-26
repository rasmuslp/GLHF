package glhf.test;

import glhf.client.GlhfClient;
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

		GlhfClient glhfClient = new GlhfClient();
		glhfClient.connect( InetAddress.getByName( "localhost" ), 55100 );

		try {
			Thread.sleep( 200 );
		} catch ( InterruptedException e1 ) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		glhfClient.setName( "Cool burger" );

		while ( true ) {
			try {
				Thread.sleep( 1000 );
				System.out.println( "-----" );
				Map< Integer, Player > players = glhfClient.getPlayers();
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
