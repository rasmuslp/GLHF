package glhf.client;

import glhf.message.MessageType;
import glhf.message.common.ChatMessage;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;
import glhf.message.server.ReadyStatusMessage;
import glhf.server.Server;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

/**
 * Parses {@link Message}s from the {@link Server}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ClientMessageParser extends AbstractMessageParser< MessageType > {

	public ClientMessageParser() {
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
				break;
			case S_IDS:
				message = IdsMessage.parse( payload );
				break;
			case S_PING_STATUS:
				break;
			case S_READY_STATUS:
				message = ReadyStatusMessage.parse( payload );
				break;
			case S_NAMES:
				message = NamesMessage.parse( payload );
				break;
			case C_READY:
			case C_NAME:
			case C_NAMES:
				Log.error( "GLHF", "Client received Client MessageType. This makes no sense. Type was: " + messageType );
				break;
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
