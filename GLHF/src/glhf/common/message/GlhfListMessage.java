package glhf.common.message;

import java.util.List;

import crossnet.message.AbstractListMessage;

public abstract class GlhfListMessage< T > extends AbstractListMessage< T, GlhfMessageType > {

	public GlhfListMessage( GlhfMessageType messageType, List< T > list ) {
		super( messageType, list );
	}

}
