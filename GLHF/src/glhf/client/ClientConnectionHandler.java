package glhf.client;

import glhf.common.entity.single.IntegerEntity;
import glhf.common.entity.tuple.IdBooleanEntity;
import glhf.common.entity.tuple.IdIntegerEntity;
import glhf.common.entity.tuple.IdStringEntity;
import glhf.common.message.GlhfMessage;
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
 * Handles the {@link Connection} of the {@link GlhfClient}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ClientConnectionHandler extends PlayerHandler implements ConnectionListener {

	/**
	 * Reference to the Player objects that constitutes this {@link GlhfClient}.
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
		if ( !( message instanceof GlhfMessage ) ) {
			return;
		}

		switch ( ( (GlhfMessage) message ).getGlhfType() ) {

		// Server Messages

			case S_IDS: {
				IdsMessage idsMessage = (IdsMessage) message;

				// Convert to set.
				Set< Integer > newIds = new HashSet<>();
				for ( IntegerEntity integerEntity : idsMessage.getList() ) {
					newIds.add( integerEntity.get() );
				}

				// Remove IDs that are no longer there.
				Set< Integer > oldIds = new HashSet<>( this.players.keySet() );
				for ( Integer id : oldIds ) {
					if ( !newIds.contains( id ) ) {
						this.removePlayer( id );
					}
				}

				// Add new IDs.
				for ( Integer id : newIds ) {
					if ( !this.players.keySet().contains( id ) ) {
						this.addPlayer( id );
					}
				}
				break;
			}

			case S_CONNECTION_CHANGE: {
				ConnectionChangeMessage connectionChangeMessage = (ConnectionChangeMessage) message;
				int id = connectionChangeMessage.getID();

				// Add / remove player 
				if ( connectionChangeMessage.didConnect() ) {
					this.addPlayer( id );
				} else {
					this.removePlayer( id );
				}
				break;
			}

			case S_NAMES: {
				NamesMessage namesMessage = (NamesMessage) message;

				// Update local storage.
				for ( IdStringEntity idName : namesMessage.getList() ) {
					this.updateName( idName.getId(), idName.getEntity().get() );
				}
				break;
			}

			case S_PINGS: {
				PingsMessage pingsMessage = (PingsMessage) message;

				// Update local storage.
				for ( IdIntegerEntity idPing : pingsMessage.getList() ) {
					this.updatePing( idPing.getId(), idPing.getEntity().get() );
				}
				break;
			}

			case S_READYS: {
				ReadysMessage readysMessage = (ReadysMessage) message;

				// Update local storage.
				for ( IdBooleanEntity idReady : readysMessage.getList() ) {
					this.updateReady( idReady.getId(), idReady.getEntity().get() );
				}
				break;
			}

			// Client Messages

			case C_NAME:
			case C_READY:
				Log.warn( "GLHF", "Got unexpected Message Type: " + message.getMessageClass() );
				break;

			// Common Messages

			case CHAT: {
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
				break;
			}

			case TIERED:
			default:
				// Ignored
				break;
		}
	}

	@Override
	public void idle( Connection connection ) {
		// Ignored
	}

	/**
	 * @return The Player objects that constitutes this {@link GlhfClient}.
	 */
	public Player getPlayer() {
		return this.self;
	}

}
