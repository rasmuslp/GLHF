package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.EntityList;
import glhf.common.entity.tuple.IdIntegerEntity;
import glhf.common.message.GlhfEntityMessage;
import glhf.common.message.GlhfMessageType;

/**
 * Pings of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class PingsMessage extends GlhfEntityMessage< EntityList< IdIntegerEntity > > {

	public PingsMessage( EntityList< IdIntegerEntity > list ) {
		super( GlhfMessageType.S_PINGS, list );
	}

}
