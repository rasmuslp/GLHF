package glhf.test;

import glhf.common.entity.EntityList;
import glhf.common.entity.list.IntegerList;
import glhf.common.entity.single.IntegerEntity;
import glhf.common.entity.tuple.IdBooleanEntity;
import glhf.common.entity.tuple.IdStringEntity;
import glhf.common.message.GlhfMessageParser;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.ReadysMessage;
import crossnet.log.Log;
import crossnet.log.LogLevel;
import crossnet.message.MessageParser;
import crossnet.message.crossnet.CrossNetMessageParser;
import crossnet.util.ByteArrayReader;
import crossnet.util.CrossNetUtil;

public class Testing {

	public static void main( String[] args ) {
		Log.set( LogLevel.TRACE );
		MessageParser messageParser = new CrossNetMessageParser();
		messageParser.setTieredMessageParser( new GlhfMessageParser() );

		// -- NamesMessage
		EntityList< IdStringEntity > namesList = new EntityList<>();
		namesList.add( new IdStringEntity( 1, "1" ) );
		namesList.add( new IdStringEntity( 2, "roflkartoffelabekatten" ) );

		NamesMessage namesMessage = new NamesMessage( namesList );
		byte[] namesBytes = namesMessage.getBytes();

		System.out.println( CrossNetUtil.bytesToHex( namesBytes ) );

		NamesMessage namesMessageParsed = (NamesMessage) messageParser.parseData( new ByteArrayReader( namesBytes ) );
		for ( IdStringEntity idTuple : namesMessageParsed.getEntity() ) {
			System.out.println( "ID: " + idTuple.getId() + " Name: " + idTuple.getEntity().get() );
		}

		// -- IdsMessage

		IntegerList listInt = new IntegerList();
		listInt.add( 1 );
		listInt.add( 2 );
		listInt.add( -1 );

		IdsMessage idsMessage = new IdsMessage( listInt );
		byte[] idsBytes = idsMessage.getBytes();

		System.out.println( CrossNetUtil.bytesToHex( idsBytes ) );

		IdsMessage idsMessageParsed = (IdsMessage) messageParser.parseData( new ByteArrayReader( idsBytes ) );
		for ( IntegerEntity integerEntity : idsMessageParsed.getEntity() ) {
			System.out.println( "ID: " + integerEntity.get() );
		}

		// ReadysMessage

		EntityList< IdBooleanEntity > listReady = new EntityList<>();
		listReady.add( new IdBooleanEntity( 1, false ) );
		listReady.add( new IdBooleanEntity( 4, true ) );

		ReadysMessage readysMessage = new ReadysMessage( 5, 3, listReady );
		byte[] readyBytes = readysMessage.getBytes();

		System.out.println( CrossNetUtil.bytesToHex( readyBytes ) );

		ReadysMessage readysMessageParsed = (ReadysMessage) messageParser.parseData( new ByteArrayReader( readyBytes ) );
		System.out.println( "No. ready: " + readysMessageParsed.getNoReady() );
		System.out.println( "No. not ready: " + readysMessageParsed.getNoNotReady() );
		for ( IdBooleanEntity idTuple : readysMessageParsed.getEntity() ) {
			System.out.println( "ID: " + idTuple.getId() + " Ready: " + idTuple.getEntity().get() );
		}

	}
}
