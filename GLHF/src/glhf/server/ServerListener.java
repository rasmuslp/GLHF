package glhf.server;

import glhf.common.User;
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

	public ServerListener( final Server server ) {
		this.server = server;
	}

	@Override
	public void connected( Connection connection ) {
		System.out.println( connection + " connected." );

		// Store locally
		int id = connection.getID();
		Map< Integer, User > users = this.server.getUsers();
		if ( users.containsKey( id ) ) {
			Log.warn( "GLHF", "Server already had the user with id '" + id + "' in its list." );
		}
		users.put( id, new User( id ) );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, true );
		this.server.sendToAllExcept( id, connectionChangeMessage );

		// Send complete ID list to new connection.
		List< Integer > ids = new ArrayList<>();
		List< IdTuple< String >> names = new ArrayList<>();
		for ( User user : users.values() ) {
			ids.add( user.getID() );
			names.add( new IdTuple<>( user.getID(), user.getName() ) );
		}

		connection.send( new IdsMessage( ids ) );
		connection.send( new NamesMessage( names ) );

		//TODO Announce connected.
	}

	@Override
	public void disconnected( Connection connection ) {
		System.out.println( connection + " disconnected." );

		// Remove from local store.
		int id = connection.getID();
		Map< Integer, User > users = this.server.getUsers();
		if ( !users.containsKey( id ) ) {
			Log.warn( "GLHF", "Server didn't have the user with id '" + id + "' in its list." );
		}
		users.remove( id );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( connection.getID(), false );
		this.server.sendToAllExcept( connection.getID(), connectionChangeMessage );

		//TODO Announce disconnected.
	}

	@Override
	public void received( Connection connection, Message message ) {
		int id = connection.getID();
		Map< Integer, User > users = this.server.getUsers();

		System.out.println( connection + " received: " + message.getClass().getSimpleName() );
		if ( message instanceof SetNameMessage ) {
			SetNameMessage setNameMessage = (SetNameMessage) message;
			String name = setNameMessage.getName();

			if ( !users.containsKey( id ) ) {
				Log.error( "GLHF", "Server didn't have the user with id '" + id + "' in its list." );
			}

			users.get( id ).setName( name );

			IdTuple< String > idName = new IdTuple<>( id, name );
			List< IdTuple< String > > names = new ArrayList<>();
			names.add( idName );
			NamesMessage namesMessage = new NamesMessage( names );
			this.server.sendToAll( namesMessage );

			//TODO Announce something?
		} else if ( message instanceof SetReadyMessage ) {
			SetReadyMessage setReadyMessage = (SetReadyMessage) message;
			boolean ready = setReadyMessage.isReady();

			if ( !users.containsKey( id ) ) {
				Log.error( "GLHF", "Server didn't have the user with id '" + id + "' in its list." );
			}

			users.get( id ).setReady( ready );

			int noReady = 0;
			int noNotReady = 0;
			for ( User user : users.values() ) {
				if ( user.isReady() ) {
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
