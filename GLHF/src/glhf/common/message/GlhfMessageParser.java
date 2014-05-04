package glhf.common.message;

import glhf.common.entity.single.IntegerEntity;
import glhf.common.entity.tuple.IdBooleanEntity;
import glhf.common.entity.tuple.IdIntegerEntity;
import glhf.common.entity.tuple.IdStringEntity;
import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

/**
 * Parses {@link Message}s of {@link GlhfMessageType}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class GlhfMessageParser extends AbstractMessageParser< GlhfMessageType > {

	public GlhfMessageParser() {
		super( GlhfMessageType.class );
	}

	@Override
	protected Message parseType( GlhfMessageType messageType, ByteArrayReader payload ) {
		Message message = null;

		try {
			switch ( messageType ) {

			// Server Messages

				case S_IDS: {
					List< IntegerEntity > list = new ArrayList<>();
					int count = payload.readInt();
					for ( int i = 0; i < count; i++ ) {
						int id = payload.readInt();
						list.add( new IntegerEntity( id ) );
					}
					message = new IdsMessage( list );
					break;
				}

				case S_CONNECTION_CHANGE: {
					int id = payload.readInt();
					boolean didConnect = payload.readBoolean();
					message = new ConnectionChangeMessage( id, didConnect );
					break;
				}

				case S_NAMES: {
					List< IdStringEntity > tuples = new ArrayList<>();
					int count = payload.readInt();
					for ( int i = 0; i < count; i++ ) {
						int id = payload.readInt();
						String name = payload.readString255();
						tuples.add( new IdStringEntity( id, name ) );
					}
					message = new NamesMessage( tuples );
					break;
				}

				case S_PINGS: {
					List< IdIntegerEntity > tuples = new ArrayList<>();
					int count = payload.readInt();
					for ( int i = 0; i < count; i++ ) {
						int id = payload.readInt();
						int ping = payload.readInt();
						tuples.add( new IdIntegerEntity( id, ping ) );
					}
					message = new PingsMessage( tuples );
					break;
				}

				case S_READYS: {
					int noReady = payload.readInt();
					int noNotReady = payload.readInt();
					int count = payload.readInt();
					List< IdBooleanEntity > tuples = new ArrayList<>();
					for ( int i = 0; i < count; i++ ) {
						int id = payload.readInt();
						boolean ready = payload.readBoolean();
						tuples.add( new IdBooleanEntity( id, ready ) );
					}
					message = new ReadysMessage( noReady, noNotReady, tuples );
					break;
				}

				// Client Messages

				case C_NAME: {
					String name = payload.readString255();
					message = new SetNameMessage( name );
					break;
				}

				case C_READY: {
					boolean ready = payload.readBoolean();
					message = new SetReadyMessage( ready );
					break;
				}

				// Common Messages

				case CHAT: {
					int senderId = payload.readInt();
					int receiverId = payload.readInt();
					String chat = payload.readString255();
					message = new ChatMessage( senderId, chat, receiverId );
					break;
				}

				case TIERED:
					if ( this.tieredMessageParser != null ) {
						message = this.tieredMessageParser.parseData( payload );
					} else {
						Log.warn( "GLHF", "No tiered parser: Cannot parse content of TieredGlhfMessage." );
					}
					break;

				default:
					Log.error( "GLHF", "Unknown GlhfMessageType, cannot parse: " + messageType );
					break;
			}
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing GlhfMessage of type:" + messageType, e );
		}

		return message;
	}
}
