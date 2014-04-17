package glhf.message.server;

import glhf.message.GlhfMessage;
import glhf.message.GlhfMessageType;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * This Message announces the connect or disconnect of a {@link Client}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ConnectionChangeMessage extends GlhfMessage {

	/**
	 * The connection ID of the {@link Client}.
	 */
	private final int connectionId;

	/**
	 * {@code True} iff the {@link Client} connected, false if it disconnected.
	 */
	private final boolean didConnect;

	/**
	 * Announces the connection change for a {@link Client}.
	 * 
	 * @param connectionId
	 *            The ID of the {@link Client} that has changed connection state.
	 * @param didConnect
	 *            {@code True} iff the {@link Client} connected, false if it disconnected.
	 */
	public ConnectionChangeMessage( final int connectionId, final boolean didConnect ) {
		super( GlhfMessageType.S_CONNECTION_CHANGE );
		this.connectionId = connectionId;
		this.didConnect = didConnect;
	}

	/**
	 * Gets the connection ID.
	 * 
	 * @return The connection ID.
	 */
	public int getConnectionID() {
		return this.connectionId;
	}

	/**
	 * @return {@code True} iff the {@link Client} connected, false if it disconnected.
	 */
	public boolean didConnect() {
		return this.didConnect;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.connectionId );
		to.writeBoolean( this.didConnect );
	}

	/**
	 * Construct an ConnectionChangeMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed ConnectionChangeMessage.
	 */
	public static ConnectionChangeMessage parse( ByteArrayReader payload ) {
		try {
			int connectionId = payload.readInt();
			boolean didConnect = payload.readBoolean();
			return new ConnectionChangeMessage( connectionId, didConnect );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing ConnectionChangeMessage:", e );
		}

		return null;
	}

}
