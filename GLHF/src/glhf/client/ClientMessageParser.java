package glhf.client;

import glhf.message.GlhfMessageType;
import glhf.message.common.ChatMessage;
import glhf.message.server.ConnectionChangeMessage;
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
public class ClientMessageParser extends AbstractMessageParser< GlhfMessageType > {

	public ClientMessageParser() {
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
				message = ConnectionChangeMessage.parse( payload );
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
				Log.error( "GLHF", "Client received Client GlhfMessageType. This makes no sense. Type was: " + glhfMessageType );
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
				Log.error( "GLHF", "Unknown GlhfMessageType, cannot parse: " + glhfMessageType );
				break;
		}

		return message;
	}

}
