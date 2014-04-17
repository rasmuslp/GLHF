package glhf.test;

import glhf.server.Server;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.log.LogLevel;

public class StartGLHFServer {

	public static void main( String[] args ) throws IOException {
		Log.set( LogLevel.TRACE );

		Server server = new Server();
		server.bind( 55100 );
	}

}
