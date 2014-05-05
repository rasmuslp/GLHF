package glhf.common.entity.list;

import glhf.common.entity.EntityList;
import glhf.common.entity.single.StringEntity;

/**
 * A specialisation that handles String.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
@SuppressWarnings( "serial" )
public class StringList extends EntityList< StringEntity > {

	/**
	 * Adds a String and automatically boxes it in a StringEntity for serialisation.
	 * 
	 * @param string
	 *            The String to add.
	 * @return {@code True} iff underlying Collection changed.
	 */
	public boolean add( String string ) {
		return this.add( new StringEntity( string ) );
	}
}
