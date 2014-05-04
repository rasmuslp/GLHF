package glhf.common.message.client;

import glhf.common.entity.single.BooleanEntity;
import glhf.common.message.GlhfMessageType;
import glhf.common.message.type.EntityMessage;

public class SetReadyMessage extends EntityMessage< BooleanEntity > {

	public SetReadyMessage( boolean ready ) {
		super( GlhfMessageType.C_READY, new BooleanEntity( ready ) );
	}

	public boolean isReady() {
		return this.getEntity().getObject();
	}

}
