package glhf.common.message;

import crossnet.message.AbstractMessage;

public abstract class GlhfMessage extends AbstractMessage< GlhfMessageType > {

	public GlhfMessage( GlhfMessageType glhfMessageType ) {
		super( glhfMessageType );
	}

}
