package glhf.common.message.client;

import glhf.common.entity.single.StringEntity;
import glhf.common.message.GlhfMessageType;
import glhf.common.message.type.EntityMessage;

public class SetNameMessage extends EntityMessage< StringEntity > {

	public SetNameMessage( String name ) {
		super( GlhfMessageType.C_NAME, new StringEntity( name ) );
	}

	public String getName() {
		return this.getEntity().getObject();
	}

}
