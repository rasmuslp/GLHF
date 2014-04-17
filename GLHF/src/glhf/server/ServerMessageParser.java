package glhf.server;

import glhf.message.GlhfMessageType;
import glhf.message.client.SetNameMessage;
import glhf.message.common.ChatMessage;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

/**
 * Parses {@link Message}s from the {@link Client}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ServerMessageParser extends AbstractMessageParser< GlhfMessageType > {

	public ServerMessageParser() {
		super( GlhfMessageType.class );
	}

	@Override
	protected Message parseType( GlhfMessageType glhfMessageType, ByteArrayReader payload ) {
		Message message = null;

		switch ( glhfMessageType ) {
			case CHAT:
				message = ChatMessage.parse( payload );
				break;
			case S_CONNECTION_CHANGE:
			case S_DEFINITION:
			case S_PING_STATUS:
			case S_READY_STATUS:
			case S_NAMES:
				Log.error( "GLHF", "Server received Server GlhfMessageType. This makes no sense. Type was: " + glhfMessageType );
				break;
			case C_READY:
				break;
			case C_NAME:
				message = SetNameMessage.parse( payload );
				break;
			case C_NAMES:
				break;
			case S_IDS:
//			case DATA:
//				message = DataMessage.parse( dataReader );
//				if ( this.tieredMessageParser != null ) {
//					message = this.tieredMessageParser.parseData( message.getBytes() );
//				} else {
//					Log.warn( "GLHF", "No tiered parser: Cannot parse content of DataMessage." );
//				}
//				break;
			default:
				Log.error( "GLHF", "Unknown GlhfMessageType, cannot parse: " + glhfMessageType );
				break;
		}

		return message;
	}
}
