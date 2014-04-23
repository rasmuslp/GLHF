package glhf.client;

import glhf.common.message.IdTuple;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;
import glhf.common.player.Player;
import glhf.common.player.PlayerHandler;

import java.util.HashSet;
import java.util.Set;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.log.Log;
import crossnet.message.Message;

/**
 * Handles the {@link Connection} of the {@link Client}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ClientConnectionHandler extends PlayerHandler implements ConnectionListener {

	/**
	 * Reference to the Player objects that constitutes this {@link Client}.
	 */
	private Player self;

	@Override
	public void connected( Connection connection ) {
		this.self = this.addPlayer( connection.getID() );
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
					this.notifyChat( null, chat, this.self );
				} else {
					this.notifyChat( null, chat, null );
				}
			} else {
				Player sender = this.players.get( chatMessage.getSenderId() );
				if ( sender == null ) {
					Log.debug( "GLHF", "Chat message from Player who has left the realm. Skipping." );
					return;
				}
				if ( chatMessage.isPrivate() ) {
					this.notifyChat( sender, chat, this.self );
				} else {
					this.notifyChat( sender, chat, null );
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

	/**
	 * @return The Player objects that constitutes this {@link Client}.
	 */
	public Player getPlayer() {
		return this.self;
	}

}
