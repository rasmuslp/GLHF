package glhf.common.message;

import java.util.List;

import crossnet.message.AbstractListMessage;

/**
 * These are used internally to maintain state. Has a List as a part of the payload.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The list payload type.
 */
public abstract class GlhfListMessage< T > extends AbstractListMessage< T, GlhfMessageType > {

	public GlhfListMessage( GlhfMessageType messageType, List< T > list ) {
		super( messageType, list );
	}

}
