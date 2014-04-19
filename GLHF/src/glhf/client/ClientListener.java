package glhf.client;

import glhf.common.Player;
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
		Map< Integer, Player > players = this.client.getPlayers();

		if ( players.containsKey( id ) ) {
			Log.warn( "GLHF", "Client already had the player with id '" + id + "' in its list." );
		}

		players.put( id, new Player( id ) );

		//TODO Announce connected.
	}

	@Override
	public void disconnected( Connection connection ) {
		int id = connection.getID();
		Map< Integer, Player > players = this.client.getPlayers();

		if ( !players.containsKey( id ) ) {
			Log.warn( "GLHF", "Client didn't have the player with id '" + id + "' in its list." );
		}

		players.remove( id );

		//TODO Announce disconnected.
	}

	@Override
	public void received( Connection connection, Message message ) {
		Map< Integer, Player > players = this.client.getPlayers();

		if ( message instanceof ChatMessage ) {
			ChatMessage chatMessage = (ChatMessage) message;
			System.out.println( "CHAT: " + chatMessage.getChatMessage() );

			//TODO Announce chat
		} else if ( message instanceof IdsMessage ) {
			IdsMessage idsMessage = (IdsMessage) message;
			Set< Integer > currentPlayerIds = new HashSet<>( idsMessage.getList() );

			// Remove old
			players.keySet().retainAll( currentPlayerIds );

			// Add new
			for ( Integer id : currentPlayerIds ) {
				if ( !players.containsKey( id ) ) {
					players.put( id, new Player( id ) );
				}
			}

			//TODO Announce players changed
		} else if ( message instanceof ConnectionChangeMessage ) {
			ConnectionChangeMessage connectionChangeMessage = (ConnectionChangeMessage) message;
			int id = connectionChangeMessage.getID();

			if ( connectionChangeMessage.didConnect() ) {
				players.put( id, new Player( id ) );
			} else {
				players.remove( id );
			}

			//TODO Announce change
		} else if ( message instanceof NamesMessage ) {
			NamesMessage namesMessage = (NamesMessage) message;

			for ( IdTuple< String > idName : namesMessage.getList() ) {
				int id = idName.getId();
				if ( players.containsKey( id ) ) {
					players.get( id ).setName( idName.getValue() );
				} else {
					Log.error( "GLHF", "Can't update name for player that isn't there. Id: " + id );
				}
			}

			//TODO Announce players got new names
		} else if ( message instanceof PingsMessage ) {
			PingsMessage pingsMessage = (PingsMessage) message;

			for ( IdTuple< Integer > idPing : pingsMessage.getList() ) {
				int id = idPing.getId();
				if ( players.containsKey( id ) ) {
					players.get( id ).setPing( idPing.getValue() );
				} else {
					Log.error( "GLHF", "Can't update ping for player that isn't there. Id: " + id );
				}
			}

			//TODO Announce players got new ping
		} else if ( message instanceof ReadysMessage ) {
			ReadysMessage readysMessage = (ReadysMessage) message;

			for ( IdTuple< Boolean > idReady : readysMessage.getList() ) {
				int id = idReady.getId();
				if ( players.containsKey( id ) ) {
					players.get( id ).setReady( idReady.getValue() );
				} else {
					Log.error( "GLHF", "Can't update ready status for player that isn't there. Id: " + id );
				}
			}

			//TODO Announce players got new ready status
		}
	}

}
