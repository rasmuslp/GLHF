package glhf.common.message.client;

import glhf.client.GlhfClient;
import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * Sets the name for the {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class SetNameMessage extends GlhfMessage {

	/**
	 * The name of the {@link GlhfClient}.
	 */
	private final String name;

	public SetNameMessage( final String name ) {
		super( GlhfMessageType.C_NAME );
		if ( name == null ) {
			throw new IllegalArgumentException( "Name cannot be null." );
		}
		this.name = name;
	}

	/**
	 * @return The name of the {@link GlhfClient}.
	 */
	public String getName() {
		return this.name;
	}

	@Override
	protected void serializeGlhfPayload( ByteArrayWriter to ) throws IOException {
		to.writeString255( this.name );
	}

	/**
	 * Construct an SetNameMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed SetNameMessage.
	 */
	public static SetNameMessage parse( ByteArrayReader payload ) {
		try {
			String name = payload.readString255();
			return new SetNameMessage( name );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing SetNameMessage:", e );
		}

		return null;
	}

}
