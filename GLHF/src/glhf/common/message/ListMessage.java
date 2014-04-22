package glhf.common.message;

import java.io.IOException;
import java.util.List;

import crossnet.message.Message;
import crossnet.util.ByteArrayWriter;

/**
 * Abstract implementation of {@link Message} that has a List as as a part of the payload.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The list payload type.
 */
public abstract class ListMessage< T > extends GlhfMessage {

	/**
	 * The list payload.
	 */
	protected final List< T > list;

	public ListMessage( GlhfMessageType glhfMessageType, final List< T > list ) {
		super( glhfMessageType );
		if ( list == null ) {
			throw new IllegalArgumentException( "List cannot be null." );
		}
		this.list = list;
	}

	/**
	 * @return The list payload.
	 */
	public List< T > getList() {
		return this.list;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		this.serializeStatic( to );
		int count = this.list.size();
		to.writeInt( count );
		for ( int i = 0; i < count; i++ ) {
			this.serializeListObject( i, to );
		}
	}

	/**
	 * Serialises any extra non-dynamic information necessary.
	 * 
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	protected abstract void serializeStatic( ByteArrayWriter to ) throws IOException;

	/**
	 * Serialises the specific object of the {@link #list}.
	 * 
	 * @param atIndex
	 *            An index of the Object to serialise.
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	protected abstract void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException;

}
