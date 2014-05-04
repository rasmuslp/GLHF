package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This Message announces the connect or disconnect of a {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ConnectionChangeMessage extends GlhfMessage {

	/**
	 * The ID of the {@link GlhfClient}.
	 */
	private final int id;

	/**
	 * {@code True} iff the {@link GlhfClient} connected, false if it disconnected.
	 */
	private final boolean didConnect;

	/**
	 * Announces the connection change for a {@link GlhfClient}.
	 * 
	 * @param id
	 *            The ID of the {@link GlhfClient} that has changed connection state.
	 * @param didConnect
	 *            {@code True} iff the {@link GlhfClient} connected, false if it disconnected.
	 */
	public ConnectionChangeMessage( final int id, final boolean didConnect ) {
		super( GlhfMessageType.S_CONNECTION_CHANGE );
		this.id = id;
		this.didConnect = didConnect;
	}

	/**
	 * @return The {@link GlhfClient} ID.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * @return {@code True} iff the {@link GlhfClient} connected, false if it disconnected.
	 */
	public boolean didConnect() {
		return this.didConnect;
	}

	@Override
	protected void serializeGlhfPayload( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.id );
		to.writeBoolean( this.didConnect );
	}

}
