package glhf.common.message;

import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;
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

		switch ( messageType ) {
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
				message = SetNameMessage.parse( payload );
				break;
			case C_READY:
				message = SetReadyMessage.parse( payload );
				break;

			// Common Messages
			case CHAT:
				message = ChatMessage.parse( payload );
				break;
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

		return message;
	}

}
