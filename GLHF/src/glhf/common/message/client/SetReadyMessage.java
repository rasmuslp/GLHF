package glhf.common.message.client;

import glhf.client.GlhfClient;
import glhf.common.entity.single.BooleanEntity;
import glhf.common.message.GlhfMessageType;
import glhf.common.message.type.EntityMessage;

/**
 * Sets the ready status for the {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class SetReadyMessage extends EntityMessage< BooleanEntity > {

	public SetReadyMessage( boolean ready ) {
		super( GlhfMessageType.C_READY, new BooleanEntity( ready ) );
	}

	/**
	 * @return {@code True} iff the {@link GlhfClient} ready.
	 */
	public boolean isReady() {
		return this.getEntity().get();
	}

}
