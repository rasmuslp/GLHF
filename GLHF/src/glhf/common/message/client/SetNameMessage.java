package glhf.common.message.client;

import glhf.client.GlhfClient;
import glhf.common.entity.single.StringEntity;
import glhf.common.message.GlhfMessageType;
import glhf.common.message.type.EntityMessage;

/**
 * Sets the name for the {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class SetNameMessage extends EntityMessage< StringEntity > {

	public SetNameMessage( String name ) {
		super( GlhfMessageType.C_NAME, new StringEntity( name ) );
	}

	/**
	 * @return The name of the {@link GlhfClient}.
	 */
	public String getName() {
		return this.getEntity().get();
	}

}
