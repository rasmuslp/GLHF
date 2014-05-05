package glhf.common.message.server;

import glhf.client.GlhfClient;
import glhf.common.entity.tuple.IdBooleanEntity;
import glhf.common.message.GlhfEntityListMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;
import java.util.List;

import crossnet.util.ByteArrayWriter;

/**
 * Ready status of relevant {@link GlhfClient}s. 'Relevant' may depend on context.
 * <p>
 * NB: This may be partial within the context.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ReadysMessage extends GlhfEntityListMessage< IdBooleanEntity > {

	/**
	 * Number of ready {@link GlhfClient}s.
	 */
	private final int noReady;

	/**
	 * Number of not ready {@link GlhfClient}s.
	 */
	private final int noNotReady;

	public ReadysMessage( final int noReady, final int noNotReady, List< IdBooleanEntity > list ) {
		super( GlhfMessageType.S_READYS, list );
		this.noReady = noReady;
		this.noNotReady = noNotReady;
	}

	/**
	 * Get the number of ready {@link GlhfClient}s.
	 * 
	 * @return The number of ready {@link GlhfClient}s.
	 */
	public int getNoReady() {
		return this.noReady;
	}

	/**
	 * Get the number of not ready {@link GlhfClient}s.
	 * 
	 * @return The number of not ready {@link GlhfClient}s.
	 */
	public int getNoNotReady() {
		return this.noNotReady;
	}

	/**
	 * Determine if all relevant {@link GlhfClient}s are ready.
	 * 
	 * @return {@code True} iff there are no not ready {@link GlhfClient}s.
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
		this.list.get( atIndex ).serialise( to );
	}

}
