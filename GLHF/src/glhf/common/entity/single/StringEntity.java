package glhf.common.entity.single;

import glhf.common.entity.SingleEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This is a String wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class StringEntity extends SingleEntity< String > {

	/**
	 * Create a StringEntity that wraps a String.
	 * <p>
	 * NB: This can only wrap Strings of a limited length. Upon serialisation the String is UTF-8 converted to a byte[].
	 * The serialisation will fail if the length of the byte[] exceeds 255 bytes.
	 * 
	 * @param string
	 *            The String to wrap.
	 */
	public StringEntity( String string ) {
		super( string );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeString255( this.object );
	}

}
