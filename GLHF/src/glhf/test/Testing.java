package glhf.test;

import glhf.client.ClientMessageParser;
import glhf.message.IdTuple;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;
import glhf.message.server.ReadyStatusMessage;

import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.log.LogLevel;
import crossnet.util.CrossNetUtil;

public class Testing {

	public static void main( String[] args ) {
		Log.set( LogLevel.TRACE );
		ClientMessageParser clientMessageParser = new ClientMessageParser();

		// -- NamesMessage
		List< IdTuple< String > > namesList = new ArrayList<>();
		namesList.add( new IdTuple<>( 1, "1" ) );
		namesList.add( new IdTuple<>( 2, "roflkartoffelabekatten" ) );

		NamesMessage namesMessage = new NamesMessage( namesList );
		byte[] namesBytes = namesMessage.getBytes();

		System.out.println( CrossNetUtil.bytesToHex( namesBytes ) );

		NamesMessage namesMessageParsed = (NamesMessage) clientMessageParser.parseData( namesBytes );
		for ( IdTuple< String > idTuple : namesMessageParsed.getList() ) {
			System.out.println( "ID: " + idTuple.getId() + " Name: " + idTuple.getValue() );
		}

		// -- IdsMessage

		List< Integer > listInt = new ArrayList<>();
		listInt.add( 1 );
		listInt.add( 2 );
		listInt.add( -1 );

		IdsMessage idsMessage = new IdsMessage( listInt );
		byte[] idsBytes = idsMessage.getBytes();

		System.out.println( CrossNetUtil.bytesToHex( idsBytes ) );

		IdsMessage idsMessageParsed = (IdsMessage) clientMessageParser.parseData( idsBytes );
		for ( int i : idsMessageParsed.getList() ) {
			System.out.println( "ID: " + i );
		}

		// ReadyStatusMessage

		List< IdTuple< Boolean > > listReady = new ArrayList<>();
		listReady.add( new IdTuple<>( 1, false ) );
		listReady.add( new IdTuple<>( 4, true ) );

		ReadyStatusMessage readyStatusMessage = new ReadyStatusMessage( 5, 3, listReady );
		byte[] readyBytes = readyStatusMessage.getBytes();

		System.out.println( CrossNetUtil.bytesToHex( readyBytes ) );

		ReadyStatusMessage readyStatusMessageParsed = (ReadyStatusMessage) clientMessageParser.parseData( readyBytes );
		System.out.println( "No. ready: " + readyStatusMessageParsed.getNoReady() );
		System.out.println( "No. not ready: " + readyStatusMessageParsed.getNoNotReady() );
		for ( IdTuple< Boolean > idTuple : readyStatusMessageParsed.getList() ) {
			System.out.println( "ID: " + idTuple.getId() + " Ready: " + idTuple.getValue() );
		}

	}
}
