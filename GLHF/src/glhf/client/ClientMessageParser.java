package glhf.client;

import glhf.common.message.GlhfMessageType;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;
import glhf.server.Server;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.message.crossnet.messages.DataMessage;
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
		// Common Messages
			case CHAT:
				message = ChatMessage.parse( payload );
				break;
			case DATA:
				message = DataMessage.parse( payload );
				break;

			// Server Messages
			case S_IDS:
				message = IdsMessage.parse( payload );
				break;
			case S_CONNECTION_CHANGE:
				message = ConnectionChangeMessage.parse( payload );
				break;
			case S_NAMES:
				message = NamesMessage.parse( payload );
				break;
			case S_PINGS:
				message = PingsMessage.parse( payload );
				break;
			case S_READYS:
				message = ReadysMessage.parse( payload );
				break;

			// Client Messages
			case C_NAME:
			case C_READY:
				Log.error( "GLHF", "Client received Client GlhfMessageType. This makes no sense. Type was: " + glhfMessageType );
				break;

			default:
				Log.error( "GLHF", "Unknown GlhfMessageType, cannot parse: " + glhfMessageType );
				break;
		}

		return message;
	}

}
