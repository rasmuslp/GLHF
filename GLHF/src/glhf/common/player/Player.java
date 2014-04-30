package glhf.common.player;

import glhf.client.GlhfClient;
import glhf.server.GlhfServer;
import crossnet.Connection;

/**
 * The Player is an abstraction of a {@link GlhfClient} connected to a {@link GlhfServer}, that is represented on both
 * ends.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class Player {

	/**
	 * The ID of the {@link Player}s {@link Connection}.
	 */
	private final int id;

	/**
	 * The name.
	 */
	private String name = "Player";

	/**
	 * {@code True} iff ready.
	 */
	private boolean isReady = false;

	/**
	 * The latest ping RTT.
	 */
	private int ping = -1;

	/**
	 * Create a Player with ID.
	 * 
	 * @param id
	 *            The ID.
	 */
	public Player( final int id ) {
		this.id = id;
	}

	/**
	 * @return The ID.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * @return The name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 */
	void setName( String name ) {
		this.name = name;
	}

	/**
	 * @return The latest ping RTT.
	 */
	public int getPing() {
		return this.ping;
	}

	/**
	 * Sets the latest ping RTT.
	 * 
	 * @param ping
	 */
	void setPing( int ping ) {
		this.ping = ping;
	}

	/**
	 * @return {@code True} iff ready.
	 */
	public boolean isReady() {
		return this.isReady;
	}

	/**
	 * Set the ready status.
	 * 
	 * @param isReady
	 *            {@code True} iff ready.
	 */
	void setReady( boolean isReady ) {
		this.isReady = isReady;
	}

}
