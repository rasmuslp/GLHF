package glhf.server;

import glhf.common.message.IdTuple;
import glhf.common.message.client.SetNameMessage;
import glhf.common.message.client.SetReadyMessage;
import glhf.common.message.common.ChatMessage;
import glhf.common.message.server.ConnectionChangeMessage;
import glhf.common.message.server.IdsMessage;
import glhf.common.message.server.NamesMessage;
import glhf.common.message.server.PingsMessage;
import glhf.common.message.server.ReadysMessage;
import glhf.common.player.Player;
import glhf.server.communication.ServerCommunicationHandler;

import java.util.ArrayList;
import java.util.List;

import crossnet.Connection;
import crossnet.listener.ConnectionListener;
import crossnet.message.Message;

public class ServerConnectionHandler extends ServerCommunicationHandler implements ConnectionListener {

	private final Server server;

	public ServerConnectionHandler( final Server server ) {
		this.server = server;
	}

	@Override
	public void connected( Connection connection ) {
		int id = connection.getID();

		// Store locally.
		this.addPlayer( id );

		// Send complete ID list to new connection. Along with other available information.
		List< Integer > idList = new ArrayList<>();
		List< IdTuple< String >> nameList = new ArrayList<>();
		int noReady = 0;
		int noNotReady = 0;
		List< IdTuple< Boolean >> readyList = new ArrayList<>();
		List< IdTuple< Integer >> pingList = new ArrayList<>();
		for ( Player player : this.players.values() ) {
			idList.add( player.getID() );
			nameList.add( new IdTuple<>( player.getID(), player.getName() ) );
			readyList.add( new IdTuple<>( player.getID(), player.isReady() ) );
			if ( player.isReady() ) {
				noReady++;
			} else {
				noNotReady++;
			}
			pingList.add( new IdTuple<>( player.getID(), player.getPing() ) );
		}
		connection.send( new IdsMessage( idList ) );
		connection.send( new NamesMessage( nameList ) );
		connection.send( new ReadysMessage( noReady, noNotReady, readyList ) );
		connection.send( new PingsMessage( pingList ) );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, true );
		this.server.sendToAllExcept( id, connectionChangeMessage );
	}

	@Override
	public void disconnected( Connection connection ) {
		int id = connection.getID();

		// Remove from local storage.
		this.removePlayer( id );

		// Send notification to all other Clients.
		ConnectionChangeMessage connectionChangeMessage = new ConnectionChangeMessage( id, false );
		this.server.sendToAllExcept( id, connectionChangeMessage );
	}

	@Override
	public void received( Connection connection, Message message ) {
		int id = connection.getID();

		if ( message instanceof ChatMessage ) {
			ChatMessage chatMessage = (ChatMessage) message;
			chatMessage.setSenderId( id );
			Player sender = this.get( id );

			if ( chatMessage.isPrivate() ) {
				Player receiver = this.get( chatMessage.getReceiverId() );
				if ( receiver == null ) {
					// Receiver has left the server
					return;
				}
				//TODO: Get connection to send to receiver
				this.notifyPlayerChatPrivate( sender, chatMessage.getChat(), receiver );
			} else {
				this.notifyPlayerChat( sender, chatMessage.getChat() );
				this.server.sendToAll( chatMessage );
			}
		} else if ( message instanceof SetNameMessage ) {
			SetNameMessage setNameMessage = (SetNameMessage) message;
			String name = setNameMessage.getName();

			// Update local storage.
			this.updateName( id, name );

			// Send notification to all other Clients.
			List< IdTuple< String > > nameList = new ArrayList<>();
			nameList.add( new IdTuple<>( id, name ) );
			this.server.sendToAll( new NamesMessage( nameList ) );
		} else if ( message instanceof SetReadyMessage ) {
			SetReadyMessage setReadyMessage = (SetReadyMessage) message;
			boolean isReady = setReadyMessage.isReady();

			// Update local storage.
			this.updateReady( id, isReady );

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
			List< IdTuple< Boolean > > readyList = new ArrayList<>();
			readyList.add( new IdTuple<>( id, isReady ) );
			this.server.sendToAll( new ReadysMessage( noReady, noNotReady, readyList ) );
		}
	}

	@Override
	public void idle( Connection connection ) {
		// Ignored
	}

}
