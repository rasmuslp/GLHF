package glhf.client;

import glhf.common.Players;
import glhf.message.IdTuple;
import glhf.message.common.ChatMessage;
import glhf.message.server.ConnectionChangeMessage;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;
import glhf.message.server.PingsMessage;
import glhf.message.server.ReadysMessage;
import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
import crossnet.message.Message;

public class ClientListener extends ConnectionListenerAdapter {

	private final Client client;

	private final Players players;

	public ClientListener( final Client client, final Players players ) {
		this.client = client;
		this.players = players;
	}

	@Override
	public void connected( Connection connection ) {
		this.players.addPlayer( connection.getID() );
	}

	@Override
	public void disconnected( Connection connection ) {
		this.players.removePlayer( connection.getID() );
	}

	@Override
	public void received( Connection connection, Message message ) {
		if ( message instanceof ChatMessage ) {
			ChatMessage chatMessage = (ChatMessage) message;
			System.out.println( "CHAT: " + chatMessage.getChatMessage() );

			//TODO Announce chat
		} else if ( message instanceof IdsMessage ) {
			IdsMessage idsMessage = (IdsMessage) message;
			this.players.updatePlayers( idsMessage.getList() );
		} else if ( message instanceof ConnectionChangeMessage ) {
			ConnectionChangeMessage connectionChangeMessage = (ConnectionChangeMessage) message;
			int id = connectionChangeMessage.getID();

			if ( connectionChangeMessage.didConnect() ) {
				this.players.addPlayer( id );
			} else {
				this.players.removePlayer( id );
			}
		} else if ( message instanceof NamesMessage ) {
			NamesMessage namesMessage = (NamesMessage) message;

			for ( IdTuple< String > idName : namesMessage.getList() ) {
				this.players.updateName( idName.getId(), idName.getValue() );
			}
		} else if ( message instanceof PingsMessage ) {
			PingsMessage pingsMessage = (PingsMessage) message;

			for ( IdTuple< Integer > idPing : pingsMessage.getList() ) {
				this.players.updatePing( idPing.getId(), idPing.getValue() );
			}
		} else if ( message instanceof ReadysMessage ) {
			ReadysMessage readysMessage = (ReadysMessage) message;

			for ( IdTuple< Boolean > idReady : readysMessage.getList() ) {
				this.players.updateReady( idReady.getId(), idReady.getValue() );
			}
		}
	}

}
