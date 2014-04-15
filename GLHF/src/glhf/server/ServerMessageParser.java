package glhf.server;

import glhf.message.MessageType;
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
public class ServerMessageParser extends AbstractMessageParser< MessageType > {

	public ServerMessageParser() {
		super( MessageType.class );
	}

	@Override
	protected Message parseType( MessageType messageType, ByteArrayReader payload ) {
		Message message = null;

		switch ( messageType ) {
			case CHAT:
				message = ChatMessage.parse( payload );
				break;
			case S_DEFINITION:
			case S_PING_STATUS:
			case S_READY_STATUS:
			case S_NAMES:
				Log.error( "GLHF", "Server received Server MessageType. This makes no sense. Type was: " + messageType );
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
				Log.error( "GLHF", "Unknown MessageType, cannot parse: " + messageType );
				break;
		}

		return message;
	}
}
