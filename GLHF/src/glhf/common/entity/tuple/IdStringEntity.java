package glhf.common.entity.tuple;

import glhf.common.entity.IdSingleEntity;
import glhf.common.entity.single.StringEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This is an ID - String wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdStringEntity extends IdSingleEntity< StringEntity > {

	/**
	 * Create an ID - String tuple.
	 * <p>
	 * NB: This can only wrap Strings of a limited length. Upon serialisation the String is UTF-8 converted to a byte[].
	 * The serialisation will fail if the length of the byte[] exceeds 255 bytes.
	 * 
	 * @param id
	 *            The ID to wrap.
	 * @param string
	 *            The String to wrap.
	 */
	public IdStringEntity( final int id, String string ) {
		super( id, new StringEntity( string ) );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
