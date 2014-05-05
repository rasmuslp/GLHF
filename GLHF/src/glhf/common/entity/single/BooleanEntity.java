package glhf.common.entity.single;

import glhf.common.entity.SingleEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This is a boolean wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class BooleanEntity extends SingleEntity< Boolean > {

	/**
	 * Create a BooleanEntity that wraps a boolean.
	 * 
	 * @param bool
	 *            The boolean to wrap.
	 */
	public BooleanEntity( boolean bool ) {
		super( bool );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeBoolean( this.object );
	}

}
