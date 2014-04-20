package glhf.common.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crossnet.log.Log;

public class PlayerHandler {

	protected final Map< Integer, Player > players = new HashMap<>();

	protected final List< PlayerListener > listeners = new ArrayList<>();

	@Deprecated
	public Map< Integer, Player > getPlayers() {
		return this.players;
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

	protected void notifyConnected( Player player ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.connected( player );
		}
	}

	protected void notifyDisconnected( Player player ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.disconnected( player );
		}
	}

	protected void notifyPlayerUpdated( Player player ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.updated( player );
		}
	}

	/**
	 * Notify listeners of a chat from sender to receiver.
	 * 
	 * @param sender
	 *            The sender of the message. Iff this is {@code null}, then it is from the {@link Server}..
	 * @param chat
	 *            The chat message.
	 * @param receiver
	 *            The recipient of the message. Iff this is {@code null}, then it is public.
	 */
	protected void notifyChat( Player sender, String chat, Player receiver ) {
		for ( PlayerListener listener : this.listeners ) {
			listener.chat( sender, chat, receiver );
		}
	}

}
