package glhf.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import crossnet.log.Log;

public class Players {

	private final Map< Integer, Player > players = new HashMap<>();

	private final List< PlayerListener > playerListeners = new ArrayList<>();

	public Map< Integer, Player > getPlayers() {
		return this.players;
	}

	public void updatePlayers( List< Integer > ids ) {
		Set< Integer > newIds = new HashSet<>( ids );

		// Remove ids that are no longer there
		Set< Integer > oldIds = new HashSet<>( this.players.keySet() );
		for ( Integer id : oldIds ) {
			if ( !newIds.contains( id ) ) {
				this.removePlayer( id );
			}
		}

		// Add new ids
		for ( Integer id : newIds ) {
			if ( !this.players.keySet().contains( id ) ) {
				this.addPlayer( id );
			}
		}
	}

	public Player addPlayer( int id ) {
		Player player = new Player( id );

		if ( this.players.put( id, player ) != null ) {
			Log.error( "GLHF", "Players already had the player with id '" + id + "' in its list. Adding and overriding." );
		}

		this.notifyConnected( player );

		//TODO: Should this just be void ?
		return player;
	}

	public void removePlayer( int id ) {
		Player player = this.players.remove( id );

		if ( player == null ) {
			Log.error( "GLHF", "Players didn't have the player with id '" + id + "' in its list. Cannot remove." );
		} else {
			this.notifyDisconnected( player );
		}
	}

	public void updateName( int id, String name ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "Players didn't have the player with id '" + id + "' in its list. Cannot update name." );
			return;
		}

		player.setName( name );

		this.notifyPlayerUpdated( player );
	}

	public void updatePing( int id, int ping ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "Players didn't have the player with id '" + id + "' in its list. Cannot update ping." );
			return;
		}

		player.setPing( ping );

		this.notifyPlayerUpdated( player );
	}

	public void updateReady( int id, boolean isReady ) {
		Player player = this.players.get( id );

		if ( player == null ) {
			Log.error( "GLHF", "Players didn't have the player with id '" + id + "' in its list. Cannot update ready." );
			return;
		}

		player.setReady( isReady );

		this.notifyPlayerUpdated( player );
	}

	/**
	 * Adds a listener. A listener cannot be added multiple times.
	 * 
	 * @param playerListener
	 *            The listener to add.
	 */
	public void addPlayerListener( PlayerListener playerListener ) {
		if ( playerListener == null ) {
			throw new IllegalArgumentException( "PlayerListener cannot be null." );
		}

		if ( this.playerListeners.contains( playerListener ) ) {
			return;
		}

		this.playerListeners.add( playerListener );
	}

	/**
	 * Removes a listener.
	 * 
	 * @param playerListener
	 *            The listener to remove.
	 */
	public void removePlayerListener( PlayerListener playerListener ) {
		if ( playerListener == null ) {
			throw new IllegalArgumentException( "PlayerListener cannot be null." );
		}

		this.playerListeners.remove( playerListener );
	}

	private void notifyConnected( Player player ) {
		for ( PlayerListener playerListener : this.playerListeners ) {
			playerListener.connected( player );
		}
	}

	private void notifyDisconnected( Player player ) {
		for ( PlayerListener playerListener : this.playerListeners ) {
			playerListener.disconnected( player );
		}
	}

	private void notifyPlayerUpdated( Player player ) {
		for ( PlayerListener playerListener : this.playerListeners ) {
			playerListener.playerUpdated( player );
		}
	}

}
