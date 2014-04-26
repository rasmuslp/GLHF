package glhf.test;

import glhf.server.GlhfServer;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.log.LogLevel;

public class StartGLHFServer {

	public static void main( String[] args ) throws IOException {
		Log.set( LogLevel.TRACE );

		GlhfServer glhfServer = new GlhfServer();
		glhfServer.bind( 55100 );
	}

}
