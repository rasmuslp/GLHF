package glhf.common.message;

import crossnet.message.AbstractMessage;
import crossnet.message.Message;

/**
 * These are used internally to maintain state and wrap external {@link Message}s.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public abstract class GlhfMessage extends AbstractMessage< GlhfMessageType > {

	public GlhfMessage( GlhfMessageType glhfMessageType ) {
		super( glhfMessageType );
	}

}
