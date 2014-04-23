package glhf.server;

import glhf.common.message.GlhfMessage;
import glhf.common.message.common.DataMessage;
import crossnet.Connection;
import crossnet.log.Log;
import crossnet.message.Message;
import crossnet.message.crossnet.CrossNetMessage;

/**
 * Extension of the CrossNet Connection.
 * <p>
 * Overrides the {@link Connection#send(Message)}: If the Message sent is not a {@link GlhfMessage} or a
 * {@link CrossNetMessage}, it will be wrapped in a {@link DataMessage} for transportation.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class GlhfConnection extends Connection {

	@Override
	public int send( Message message ) {
		if ( message == null ) {
			throw new IllegalArgumentException( "Cannot send null." );
		}

		String messageClass = message.getClass().getSimpleName();
		boolean wrapped = false;
		if ( !( message instanceof GlhfMessage || message instanceof CrossNetMessage ) ) {
			// Wrap message in DataMessage
			byte[] messageData = message.getBytes();
			message = new DataMessage( messageData );
			wrapped = true;
		}

		int length = super.send( message );
		if ( length == 0 ) {
			Log.trace( "GLHF", this + " had nothing to send." );
		} else if ( wrapped ) {
			Log.debug( "GLHF", this + " sent: " + messageClass + " (" + length + ")" );
		}
		return length;
	}

}
