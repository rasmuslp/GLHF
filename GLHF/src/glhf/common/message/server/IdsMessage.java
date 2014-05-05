package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.list.IntegerList;
import glhf.common.message.GlhfEntityMessage;
import glhf.common.message.GlhfMessageType;

/**
 * A list of IDs of all relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdsMessage extends GlhfEntityMessage< IntegerList > {

	public IdsMessage( IntegerList list ) {
		super( GlhfMessageType.S_IDS, list );
	}

}
