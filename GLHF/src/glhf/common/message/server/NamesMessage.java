package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.EntityList;
import glhf.common.entity.tuple.IdStringEntity;
import glhf.common.message.GlhfEntityMessage;
import glhf.common.message.GlhfMessageType;

/**
 * Names of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class NamesMessage extends GlhfEntityMessage< EntityList< IdStringEntity > > {

	public NamesMessage( EntityList< IdStringEntity > names ) {
		super( GlhfMessageType.S_NAMES, names );
	}

}
