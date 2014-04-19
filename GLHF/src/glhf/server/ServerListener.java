package glhf.server;

import glhf.common.Player;
import glhf.common.Players;
import glhf.message.IdTuple;
import glhf.message.client.SetNameMessage;
import glhf.message.client.SetReadyMessage;
import glhf.message.server.ConnectionChangeMessage;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;
import glhf.message.server.ReadysMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;

public class ServerListener extends ConnectionListenerAdapter {

	private final Server server;

	private final Players players;

	public ServerListener( final Server server, final Players players ) {
		this.server = server;
		this.players = players;
	}

	@Override
	public void connected( Connection connection ) {
		System.out.println( connection + " connected." );

		int id = connection.getID();

		// Store locally
		this.players.addPlayer( connection.getID() );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, true );
		this.server.sendToAllExcept( id, connectionChangeMessage );

		// Send complete ID list to new connection.
		List< Integer > ids = new ArrayList<>();
		List< IdTuple< String >> names = new ArrayList<>();
		for ( Player player : this.players.getPlayers().values() ) {
			ids.add( player.getID() );
			names.add( new IdTuple<>( player.getID(), player.getName() ) );
		}
		connection.send( new IdsMessage( ids ) );
		connection.send( new NamesMessage( names ) );
	}

	@Override
	public void disconnected( Connection connection ) {
		System.out.println( connection + " disconnected." );

		int id = connection.getID();

		// Remove from local store.
		this.players.removePlayer( id );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, false );
		this.server.sendToAllExcept( id, connectionChangeMessage );
	}

	@Override
	public void received( Connection connection, Message message ) {
		int id = connection.getID();
		Map< Integer, Player > players = this.server.getPlayers();

		System.out.println( connection + " received: " + message.getClass().getSimpleName() );
		if ( message instanceof SetNameMessage ) {
			SetNameMessage setNameMessage = (SetNameMessage) message;
			String name = setNameMessage.getName();

			if ( !players.containsKey( id ) ) {
				Log.error( "GLHF", "Server didn't have the player with id '" + id + "' in its list." );
			}

			players.get( id ).setName( name );

			IdTuple< String > idName = new IdTuple<>( id, name );
			List< IdTuple< String > > names = new ArrayList<>();
			names.add( idName );
			NamesMessage namesMessage = new NamesMessage( names );
			this.server.sendToAll( namesMessage );

			//TODO Announce something?
		} else if ( message instanceof SetReadyMessage ) {
			SetReadyMessage setReadyMessage = (SetReadyMessage) message;
			boolean ready = setReadyMessage.isReady();

			if ( !players.containsKey( id ) ) {
				Log.error( "GLHF", "Server didn't have the player with id '" + id + "' in its list." );
			}

			players.get( id ).setReady( ready );

			int noReady = 0;
			int noNotReady = 0;
			for ( Player player : players.values() ) {
				if ( player.isReady() ) {
					noReady++;
				} else {
					noNotReady++;
				}
			}

			IdTuple< Boolean > idReady = new IdTuple<>( id, ready );
			List< IdTuple< Boolean > > readies = new ArrayList<>();
			readies.add( idReady );
			ReadysMessage readysMessage = new ReadysMessage( noReady, noNotReady, readies );
			this.server.sendToAll( readysMessage );

			//TODO Announce something?			
		}
	}

}
