package glhf.client;

import glhf.client.communication.ClientCommunicationHandler;
import glhf.common.message.IdTuple;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;
import glhf.common.player.Player;

import java.util.HashSet;
import java.util.Set;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.message.Message;

public class ClientConnectionHandler extends ClientCommunicationHandler implements ConnectionListener {

	@Override
	public void connected( Connection connection ) {
		this.addPlayer( connection.getID() );
	}

	@Override
	public void disconnected( Connection connection ) {
		this.removePlayer( connection.getID() );
	}

	@Override
	public void received( Connection connection, Message message ) {
		if ( message instanceof ChatMessage ) {
			ChatMessage chatMessage = (ChatMessage) message;
			String chat = chatMessage.getChat();

			if ( chatMessage.isServerMessage() ) {
				if ( chatMessage.isPrivate() ) {
					this.notifyServerChatPrivate( chat );
				} else {
					this.notifyServerChat( chat );
				}
			} else {
				Player sender = this.get( chatMessage.getSenderId() );
				if ( chatMessage.isPrivate() ) {
					this.notifyPlayerChatPrivate( sender, chat );
				} else {
					this.notifyPlayerChat( sender, chat );
				}
			}
		} else if ( message instanceof IdsMessage ) {
			IdsMessage idsMessage = (IdsMessage) message;
			Set< Integer > newIds = new HashSet<>( idsMessage.getList() );

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
		} else if ( message instanceof ConnectionChangeMessage ) {
			ConnectionChangeMessage connectionChangeMessage = (ConnectionChangeMessage) message;
			int id = connectionChangeMessage.getID();

			if ( connectionChangeMessage.didConnect() ) {
				this.addPlayer( id );
			} else {
				this.removePlayer( id );
			}
		} else if ( message instanceof NamesMessage ) {
			NamesMessage namesMessage = (NamesMessage) message;

			for ( IdTuple< String > idName : namesMessage.getList() ) {
				this.updateName( idName.getId(), idName.getValue() );
			}
		} else if ( message instanceof PingsMessage ) {
			PingsMessage pingsMessage = (PingsMessage) message;

			for ( IdTuple< Integer > idPing : pingsMessage.getList() ) {
				this.updatePing( idPing.getId(), idPing.getValue() );
			}
		} else if ( message instanceof ReadysMessage ) {
			ReadysMessage readysMessage = (ReadysMessage) message;

			for ( IdTuple< Boolean > idReady : readysMessage.getList() ) {
				this.updateReady( idReady.getId(), idReady.getValue() );
			}
		}
	}

	@Override
	public void idle( Connection connection ) {
		// Ignored
	}

}
