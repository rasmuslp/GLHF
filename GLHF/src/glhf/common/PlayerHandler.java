package glhf.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crossnet.log.Log;

public class PlayerHandler< T extends PlayerListener > {

	protected final Map< Integer, Player > players = new HashMap<>();

	protected final List< T > listeners = new ArrayList<>();

	@Deprecated
	public Map< Integer, Player > getPlayers() {
		return this.players;
	}

	@Deprecated
	protected Player get( int id ) {
		Player player = this.players.get( id );
		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot get." );
		}
		return player;
	}

	protected Player addPlayer( int id ) {
		Player player = new Player( id );

		if ( this.players.put( id, player ) != null ) {
			Log.error( "GLHF", "PlayerHandler already had the player with id '" + id + "' in its list. Adding and overriding." );
		}

		this.notifyConnected( player );

		//TODO: Should this just be void ?
		return player;
	}

	protected void removePlayer( int id ) {
		Player player = this.players.remove( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot remove." );
		} else {
			this.notifyDisconnected( player );
		}
	}

	protected void updateName( int id, String name ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot update name." );
			return;
		}

		player.setName( name );

		this.notifyPlayerUpdated( player );
	}

	protected void updatePing( int id, int ping ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "PlayerHandler didn't have the player with id '" + id + "' in its list. Cannot update ping." );
			return;
		}

		player.setPing( ping );

		this.notifyPlayerUpdated( player );
	}

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
	public void addListener( T listener ) {
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
	public void removeListener( T listener ) {
		if ( listener == null ) {
			throw new IllegalArgumentException( "Listener cannot be null." );
		}

		this.listeners.remove( listener );
	}

	protected void notifyConnected( Player player ) {
		for ( T listener : this.listeners ) {
			listener.connected( player );
		}
	}

	protected void notifyDisconnected( Player player ) {
		for ( T listener : this.listeners ) {
			listener.disconnected( player );
		}
	}

	protected void notifyPlayerUpdated( Player player ) {
		for ( T listener : this.listeners ) {
			listener.playerUpdated( player );
		}
	}

}
