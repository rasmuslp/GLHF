package glhf.common.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crossnet.log.Log;

/**
 * Handles the {@link Player}s.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class PlayerHandler {

	/**
	 * The current mapping of IDs to {@link Player}s.
	 */
	protected final Map< Integer, Player > players = new HashMap<>();

	/**
	 * The associated listeners.
	 */
	protected final List< PlayerListener > listeners = new ArrayList<>();

	/**
	 * NB: Do not modify this.
	 * 
	 * @return The current mapping of IDs to {@link Player}s.
	 */
	public Map< Integer, Player > getPlayers() {
		return this.players;
	}

	/**
	 * Adds and creates a new Player.
	 * 
	 * @param id
	 *            The ID of the new Player.
	 * @return The created Player.
	 */
	protected Player addPlayer( int id ) {
		Player player = new Player( id );

		if ( this.players.put( id, player ) != null ) {
			Log.error( "GLHF", "PlayerHandler already had the player with id '" + id + "' in its list. Adding and overriding." );
		}

		this.notifyConnected( player );

		return player;
	}

	/**
	 * Removes the {@link Player}, if such exists.
	 * 
	 * @param id
	 *            The ID of the Player to remove.
	 */
	protected void removePlayer( int id ) {
		Player player = this.players.remove( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot remove." );
		} else {
			this.notifyDisconnected( player );
		}
	}

	/**
	 * Update the name of a {@link Player}.
	 * 
	 * @param id
	 *            The ID of the Player.
	 * @param name
	 *            The new name of the Player.
	 */
	protected void updateName( int id, String name ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot update name." );
			return;
		}

		player.setName( name );

		this.notifyPlayerUpdated( player );
	}

	/**
	 * Update the ping RTT of a {@link Player}.
	 * 
	 * @param id
	 *            The ID of the Player.
	 * @param ping
	 *            The new ping RTT of the Player.
	 */
	protected void updatePing( int id, int ping ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot update ping." );
			return;
		}

		player.setPing( ping );

		this.notifyPlayerUpdated( player );
	}

	/**
	 * Update the ready status of a {@link Player}.
	 * 
	 * @param id
	 *            The ID of the Player.
	 * @param isReady
	 *            The new ready status of the Player.
	 */
	protected void updateReady( int id, boolean isReady ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot update ready." );
			return;
		}

		player.setReady( isReady );

		this.notifyPlayerUpdated( player );
	}

	/**
	 * Adds a listener. A listener cannot be added multiple times.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addListener( PlayerListener listener ) {
		if ( listener == null ) {
			throw new IllegalArgumentException( "Listener cannot be null." );
		}

		if ( this.listeners.contains( listener ) ) {
			return;
		}

		this.listeners.add( listener );
	}

	/**
	 * Removes a listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeListener( PlayerListener listener ) {
		if ( listener == null ) {
			throw new IllegalArgumentException( "Listener cannot be null." );
		}

		this.listeners.remove( listener );
	}

	/**
	 * Notify listeners that a new Player connected.
	 * 
	 * @see PlayerListener#connected(Player)
	 */
	protected void notifyConnected( Player player ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.connected( player );
		}
	}

	/**
	 * Notify listeners that the Player disconnected.
	 * 
	 * @see PlayerListener#disconnected(Player)
	 */
	protected void notifyDisconnected( Player player ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.disconnected( player );
		}
	}

	/**
	 * Notify listeners of a Player attribute update.
	 * 
	 * @see PlayerListener#updated(Player)
	 */
	protected void notifyPlayerUpdated( Player player ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.updated( player );
		}
	}

	/**
	 * Notify listeners of a chat from sender to receiver.
	 * 
	 * @see PlayerListener#chat(Player, String, Player)
	 */
	protected void notifyChat( Player sender, String chat, Player receiver ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.chat( sender, chat, receiver );
		}
	}

}
