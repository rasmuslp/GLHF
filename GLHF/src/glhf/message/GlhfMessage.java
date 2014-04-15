package glhf.message;

import crossnet.message.AbstractMessage;

public abstract class GlhfMessage extends AbstractMessage< MessageType > {

	public GlhfMessage( MessageType messageType ) {
		super( messageType );
	}

}
