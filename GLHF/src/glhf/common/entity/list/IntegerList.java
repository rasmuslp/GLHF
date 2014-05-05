package glhf.common.entity.list;

import glhf.common.entity.EntityList;
import glhf.common.entity.single.IntegerEntity;

/**
 * A specialisation that handles Integers.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
@SuppressWarnings( "serial" )
public class IntegerList extends EntityList< IntegerEntity > {

	/**
	 * Adds an integer and automatically boxes it in an IntegerEntity for serialisation.
	 * 
	 * @param integer
	 *            The integer to add.
	 * @return {@code True} iff underlying Collection changed.
	 */
	public boolean add( int integer ) {
		return this.add( new IntegerEntity( integer ) );
	}

}
