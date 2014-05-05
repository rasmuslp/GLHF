package glhf.common.entity.single;

import glhf.common.entity.SingleEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This is an integer wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IntegerEntity extends SingleEntity< Integer > {

	/**
	 * Create an IntegerEntity that wraps an integer.
	 * 
	 * @param integer
	 *            The integer to wrap.
	 */
	public IntegerEntity( int integer ) {
		super( integer );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.object );
	}

}
