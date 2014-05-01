package glhf.common.message.common;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;
import crossnet.message.MessageParser;

/**
 * This Message is for use together with a tiered {@link MessageParser}.
 * <p>
 * Subclass this such that it looks like {@link GlhfMessage}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public abstract class TieredGlhfMessage extends GlhfMessage {

	/**
	 * Create a new TieredGlhfMessage.
	 */
	public TieredGlhfMessage() {
		super( GlhfMessageType.TIERED );
	}

}
