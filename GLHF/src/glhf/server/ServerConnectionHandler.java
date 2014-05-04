package glhf.server;

import glhf.common.entity.single.IntegerEntity;
import glhf.common.entity.tuple.IdBooleanEntity;
import glhf.common.entity.tuple.IdIntegerEntity;
import glhf.common.entity.tuple.IdStringEntity;
import glhf.common.message.GlhfMessage;
import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.common.TieredGlhfMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;
import glhf.common.player.Player;
import glhf.common.player.PlayerHandler;

import java.util.ArrayList;
import java.util.List;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.log.Log;
import crossnet.message.Message;

/**
 * Handles the {@link Connection}s of the {@link GlhfServer}.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ServerConnectionHandler extends PlayerHandler implements ConnectionListener {

	private final GlhfServer glhfServer;

	public ServerConnectionHandler( final GlhfServer glhfServer ) {
		this.glhfServer = glhfServer;
	}

	@Override
	public void connected( Connection connection ) {
		int id = connection.getID();

		// Store locally.
		this.addPlayer( id );

		// Send complete ID list to new connection. Along with other available information.
		List< IntegerEntity > idList = new ArrayList<>();
		List< IdStringEntity > nameList = new ArrayList<>();
		int noReady = 0;
		int noNotReady = 0;
		List< IdBooleanEntity > readyList = new ArrayList<>();
		List< IdIntegerEntity > pingList = new ArrayList<>();
		for ( Player player : this.players.values() ) {
			idList.add( new IntegerEntity( player.getID() ) );
			nameList.add( new IdStringEntity( player.getID(), player.getName() ) );
			readyList.add( new IdBooleanEntity( player.getID(), player.isReady() ) );
			if ( player.isReady() ) {
				noReady++;
			} else {
				noNotReady++;
			}
			pingList.add( new IdIntegerEntity( player.getID(), player.getPing() ) );
		}
		connection.send( new IdsMessage( idList ) );
		connection.send( new NamesMessage( nameList ) );
		connection.send( new ReadysMessage( noReady, noNotReady, readyList ) );
		connection.send( new PingsMessage( pingList ) );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, true );
		this.glhfServer.sendToAllExcept( id, connectionChangeMessage );
	}

	@Override
	public void disconnected( Connection connection ) {
		int id = connection.getID();

		// Remove from local storage.
		this.removePlayer( id );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, false );
		this.glhfServer.sendToAllExcept( id, connectionChangeMessage );
	}

	@Override
	public void received( Connection connection, Message message ) {
		if ( !( message instanceof GlhfMessage ) ) {
			return;
		}

		int senderId = connection.getID();
		switch ( ( (GlhfMessage) message ).getType() ) {
			case C_NAME: {
				SetNameMessage setNameMessage = (SetNameMessage) message;
				String name = setNameMessage.getName();

				// Update local storage.
				this.updateName( senderId, name );

				// Send notification to all other Clients.
				List< IdStringEntity > nameList = new ArrayList<>();
				nameList.add( new IdStringEntity( senderId, name ) );
				this.glhfServer.sendToAll( new NamesMessage( nameList ) );

				break;
			}
			case C_READY: {
				SetReadyMessage setReadyMessage = (SetReadyMessage) message;
				boolean isReady = setReadyMessage.isReady();

				// Update local storage.
				this.updateReady( senderId, isReady );

				// Send notification to all other Clients.
				int noReady = 0;
				int noNotReady = 0;
				for ( Player player : this.players.values() ) {
					if ( player.isReady() ) {
						noReady++;
					} else {
						noNotReady++;
					}
				}
				List< IdBooleanEntity > readyList = new ArrayList<>();
				readyList.add( new IdBooleanEntity( senderId, isReady ) );
				this.glhfServer.sendToAll( new ReadysMessage( noReady, noNotReady, readyList ) );

				break;
			}
		}

		if ( message instanceof ChatMessage ) {
			ChatMessage chatMessage = (ChatMessage) message;
			chatMessage.setSenderId( senderId );
			String chat = chatMessage.getChat();
			int receiverId = chatMessage.getReceiverId();

			// Determine sender
			Player sender = this.players.get( senderId );
			if ( sender == null ) {
				Log.debug( "GLHF", "Chat message from Player who has left the realm. Skipping." );
				return;
			}

			// Notify and pass on.
			if ( chatMessage.isPrivate() ) {
				Player receiver = this.players.get( receiverId );
				if ( receiver == null ) {
					Log.debug( "GLHF", "Chat message to Player who has left the realm. Skipping." );
					return;
				}
				this.notifyChat( sender, chat, receiver );
				this.glhfServer.getConnections().get( chatMessage.getReceiverId() ).send( chatMessage );
			} else {
				this.notifyChat( sender, chat, null );
				this.glhfServer.sendToAll( chatMessage );
			}
		} else if ( ( message instanceof GlhfMessage ) && !( message instanceof TieredGlhfMessage ) ) {
			Log.warn( "GLHF", "Got unexpected Message Type: " + message.getClass().getSimpleName() );
		}
	}

	@Override
	public void idle( Connection connection ) {
		// Ignored
	}

	/**
	 * Sends all changed ping RTTs to all {@link Player}s.
	 */
	public void updatePings() {
		List< IdIntegerEntity > pingList = new ArrayList<>();
		for ( Player player : this.players.values() ) {
			int id = player.getID();
			int ping = this.glhfServer.getConnections().get( id ).getTransportLayer().getPingRoundTripTime();
			if ( ping == player.getPing() ) {
				// If ping is unchanged, skip.
				continue;
			}
			this.updatePing( id, ping );
			pingList.add( new IdIntegerEntity( id, ping ) );
		}
		this.glhfServer.sendToAll( new PingsMessage( pingList ) );
	}
}
