package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.tuple.IdBooleanEntity;
import glhf.common.message.GlhfEntityMessage;
import glhf.common.message.GlhfMessageType;

/**
 * This Message announces the connect or disconnect of a {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ConnectionChangeMessage extends GlhfEntityMessage< IdBooleanEntity > {

	/**
	 * Announces the connection change for a {@link GlhfClient}.
	 * 
	 * @param id
	 *            The ID of the {@link GlhfClient} that has changed connection state.
	 * @param didConnect
	 *            {@code True} iff the {@link GlhfClient} connected, false if it disconnected.
	 */
	public ConnectionChangeMessage( final int id, final boolean didConnect ) {
		super( GlhfMessageType.S_CONNECTION_CHANGE, new IdBooleanEntity( id, didConnect ) );
	}

	/**
	 * @return The {@link GlhfClient} ID.
	 */
	public int getID() {
		return this.getEntity().getId();
	}

	/**
	 * @return {@code True} iff the {@link GlhfClient} connected, false if it disconnected.
	 */
	public boolean didConnect() {
		return this.getEntity().getEntity().get();
	}

}
