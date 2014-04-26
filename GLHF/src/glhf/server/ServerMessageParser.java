package glhf.server;

import glhf.common.message.GlhfMessageType;
import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.common.DataMessage;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

/**
 * Parses {@link Message}s from the {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ServerMessageParser extends AbstractMessageParser< GlhfMessageType > {

	public ServerMessageParser() {
		super( GlhfMessageType.class );
	}

	@Override
	protected Message parseType( GlhfMessageType messageType, ByteArrayReader payload ) {
		Message message = null;

		switch ( messageType ) {
		// Common Messages
			case CHAT:
				message = ChatMessage.parse( payload );
				break;
			case DATA:
				message = DataMessage.parse( payload );
				break;

			// Server Messages
			case S_IDS:
			case S_CONNECTION_CHANGE:
			case S_NAMES:
			case S_PINGS:
			case S_READYS:
				Log.error( "GLHF", "Server received Server GlhfMessageType. This makes no sense. Type was: " + messageType );
				break;

			// Client Messages
			case C_READY:
				message = SetReadyMessage.parse( payload );
				break;
			case C_NAME:
				message = SetNameMessage.parse( payload );
				break;

			default:
				Log.error( "GLHF", "Unknown GlhfMessageType, cannot parse: " + messageType );
				break;
		}

		return message;
	}
}
