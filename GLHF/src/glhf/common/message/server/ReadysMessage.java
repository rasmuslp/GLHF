package glhf.common.message.server;

import glhf.common.message.GlhfMessageType;
import glhf.common.message.IdTuple;
import glhf.common.message.ListMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * Ready status of relevant {@link Client}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ReadysMessage extends ListMessage< IdTuple< Boolean > > {

	/**
	 * Number of ready {@link Client}s.
	 */
	private final int noReady;

	/**
	 * Number of not ready {@link Client}s.
	 */
	private final int noNotReady;

	public ReadysMessage( final int noReady, final int noNotReady, List< IdTuple< Boolean > > list ) {
		super( GlhfMessageType.S_READYS, list );
		this.noReady = noReady;
		this.noNotReady = noNotReady;
	}

	/**
	 * Get the number of ready {@link Client}s.
	 * 
	 * @return The number of ready {@link Client}s.
	 */
	public int getNoReady() {
		return this.noReady;
	}

	/**
	 * Get the number of not ready {@link Client}s.
	 * 
	 * @return The number of not ready {@link Client}s.
	 */
	public int getNoNotReady() {
		return this.noNotReady;
	}

	/**
	 * Determine if all relevant {@link Client}s are ready.
	 * 
	 * @return {@code True} iff there are no not ready {@link Client}s.
	 */
	public boolean allReady() {
		if ( this.noNotReady == 0 ) {
			return true;
		}

		return false;
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.noReady );
		to.writeInt( this.noNotReady );
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		IdTuple< Boolean > idTuple = this.list.get( atIndex );
		to.writeInt( idTuple.getId() );
		to.writeBoolean( idTuple.getValue() );
	}

	/**
	 * Construct an ReadysMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed ReadysMessage.
	 */
	public static ReadysMessage parse( ByteArrayReader payload ) {
		try {
			int noReady = payload.readInt();
			int noNotReady = payload.readInt();
			int count = payload.readInt();
			List< IdTuple< Boolean > > tuples = new ArrayList<>();
			for ( int i = 0; i < count; i++ ) {
				int id = payload.readInt();
				boolean ready = payload.readBoolean();
				tuples.add( new IdTuple<>( id, ready ) );
			}
			return new ReadysMessage( noReady, noNotReady, tuples );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing ReadysMessage:", e );
		}

		return null;
	}

}
