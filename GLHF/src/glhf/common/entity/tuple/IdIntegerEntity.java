package glhf.common.entity.tuple;

import glhf.common.entity.IdSingleEntity;
import glhf.common.entity.single.IntegerEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This is an ID - integer wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdIntegerEntity extends IdSingleEntity< IntegerEntity > {

	/**
	 * Create an ID - integer tuple.
	 * 
	 * @param id
	 *            The ID to wrap.
	 * @param integer
	 *            The integer to wrap.
	 */
	public IdIntegerEntity( final int id, final int integer ) {
		super( id, new IntegerEntity( integer ) );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
