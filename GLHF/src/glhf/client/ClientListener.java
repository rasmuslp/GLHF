package glhf.client;

import glhf.common.User;
import glhf.message.IdTuple;
import glhf.message.common.ChatMessage;
import glhf.message.server.ConnectionChangeMessage;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;
import glhf.message.server.PingsMessage;
import glhf.message.server.ReadysMessage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.log.Log;
import crossnet.message.Message;

public class ClientListener extends ConnectionListenerAdapter {

	private final Client client;

	public ClientListener( final Client client ) {
		this.client = client;
	}

	@Override
	public void connected( Connection connection ) {
		int id = connection.getID();
		Map< Integer, User > users = this.client.getUsers();

		if ( users.containsKey( id ) ) {
			Log.warn( "GLHF", "Client already had the user with id '" + id + "' in its list." );
		}

		users.put( id, new User( id ) );

		//TODO Announce connected.
	}

	@Override
	public void disconnected( Connection connection ) {
		int id = connection.getID();
		Map< Integer, User > users = this.client.getUsers();

		if ( !users.containsKey( id ) ) {
			Log.warn( "GLHF", "Client didn't have the user with id '" + id + "' in its list." );
		}

		users.remove( id );

		//TODO Announce disconnected.
	}

	@Override
	public void received( Connection connection, Message message ) {
		Map< Integer, User > users = this.client.getUsers();

		if ( message instanceof ChatMessage ) {
			ChatMessage chatMessage = (ChatMessage) message;
			System.out.println( "CHAT: " + chatMessage.getChatMessage() );

			//TODO Announce chat
		} else if ( message instanceof IdsMessage ) {
			IdsMessage idsMessage = (IdsMessage) message;
			Set< Integer > currentUserIds = new HashSet<>( idsMessage.getList() );

			// Remove old
			users.keySet().retainAll( currentUserIds );

			// Add new
			for ( Integer id : currentUserIds ) {
				if ( !users.containsKey( id ) ) {
					users.put( id, new User( id ) );
				}
			}

			//TODO Announce users changed
		} else if ( message instanceof ConnectionChangeMessage ) {
			ConnectionChangeMessage connectionChangeMessage = (ConnectionChangeMessage) message;
			int id = connectionChangeMessage.getID();

			if ( connectionChangeMessage.didConnect() ) {
				users.put( id, new User( id ) );
			} else {
				users.remove( id );
			}

			//TODO Announce change
		} else if ( message instanceof NamesMessage ) {
			NamesMessage namesMessage = (NamesMessage) message;

			for ( IdTuple< String > idName : namesMessage.getList() ) {
				int id = idName.getId();
				if ( users.containsKey( id ) ) {
					users.get( id ).setName( idName.getValue() );
				} else {
					Log.error( "GLHF", "Can't update name for user that isn't there. Id: " + id );
				}
			}

			//TODO Announce users got new names
		} else if ( message instanceof PingsMessage ) {
			PingsMessage pingsMessage = (PingsMessage) message;

			for ( IdTuple< Integer > idPing : pingsMessage.getList() ) {
				int id = idPing.getId();
				if ( users.containsKey( id ) ) {
					users.get( id ).setPing( idPing.getValue() );
				} else {
					Log.error( "GLHF", "Can't update ping for user that isn't there. Id: " + id );
				}
			}

			//TODO Announce users got new ping
		} else if ( message instanceof ReadysMessage ) {
			ReadysMessage readysMessage = (ReadysMessage) message;

			for ( IdTuple< Boolean > idReady : readysMessage.getList() ) {
				int id = idReady.getId();
				if ( users.containsKey( id ) ) {
					users.get( id ).setReady( idReady.getValue() );
				} else {
					Log.error( "GLHF", "Can't update ready status for user that isn't there. Id: " + id );
				}
			}

			//TODO Announce users got new ready status
		}
	}

}
