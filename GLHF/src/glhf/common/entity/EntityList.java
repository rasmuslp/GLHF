package glhf.common.entity;

import java.io.IOException;
import java.util.ArrayList;

import crossnet.util.ByteArrayWriter;

/**
 * An implementation of a list of Entities.
 * <p>
 * This object wraps a list of objects such that it is guaranteed that they can be serialised.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 * @param <T>
 *            The type of Entity in the list.
 */
@SuppressWarnings( "serial" )
public class EntityList< T extends Entity > extends ArrayList< T > implements Entity {

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		int count = this.size();
		to.writeInt( count );
		for ( int i = 0; i < count; i++ ) {
			this.get( i ).serialise( to );
		}
	}

}
