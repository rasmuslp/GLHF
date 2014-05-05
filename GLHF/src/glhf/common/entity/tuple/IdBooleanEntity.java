package glhf.common.entity.tuple;

import glhf.common.entity.IdSingleEntity;
import glhf.common.entity.single.BooleanEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * This is an ID - boolean wrapper.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class IdBooleanEntity extends IdSingleEntity< BooleanEntity > {

	/**
	 * Create an ID - boolean tuple.
	 * 
	 * @param id
	 *            The ID to wrap.
	 * @param bool
	 *            The boolean to wrap.
	 */
	public IdBooleanEntity( final int id, final boolean bool ) {
		super( id, new BooleanEntity( bool ) );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
