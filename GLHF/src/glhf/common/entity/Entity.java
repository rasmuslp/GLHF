package glhf.common.entity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

/**
 * An Entity is an object that can be serialised to a {@link ByteArrayWriter}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public interface Entity {

	/**
	 * Serialise this object to the provided destination.
	 * 
	 * @param to
	 *            The destination of the serialisation.
	 * @throws IOException
	 *             If a serialisation error occurs.
	 */
	public void serialise( ByteArrayWriter to ) throws IOException;

}
