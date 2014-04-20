package glhf.server;

import glhf.common.Player;
import glhf.common.Players;
import glhf.message.IdTuple;
import glhf.message.client.SetNameMessage;
import glhf.message.client.SetReadyMessage;
import glhf.message.common.ChatMessage;
import glhf.message.server.ConnectionChangeMessage;
import glhf.message.server.IdsMessage;
import glhf.message.server.NamesMessage;
import glhf.message.server.PingsMessage;
import glhf.message.server.ReadysMessage;

import java.util.ArrayList;
import java.util.List;

import crossnet.Connection;
import crossnet.listener.ConnectionListenerAdapter;
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
		int id = connection.getID();

		// Store locally.
		this.players.addPlayer( id );

		// Send complete ID list to new connection. Along with other available information.
		List< Integer > idList = new ArrayList<>();
		List< IdTuple< String >> nameList = new ArrayList<>();
		int noReady = 0;
		int noNotReady = 0;
		List< IdTuple< Boolean >> readyList = new ArrayList<>();
		List< IdTuple< Integer >> pingList = new ArrayList<>();
		for ( Player player : this.players.getPlayers().values() ) {
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
		this.players.removePlayer( id );

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
			Player sender = this.players.get( id );

			if ( chatMessage.isPrivate() ) {
				Player receiver = this.players.get( chatMessage.getReceiverId() );
				if ( receiver == null ) {
					// Receiver has left the server
					return;
				}
				//TODO: Get connection to send to receiver
				this.players.notifyPlayerChatToPlayer( sender, chatMessage.getChat(), receiver );
			} else {
				this.players.notifyPlayerChat( sender, chatMessage.getChat() );
				this.server.sendToAll( chatMessage );
			}
		} else if ( message instanceof SetNameMessage ) {
			SetNameMessage setNameMessage = (SetNameMessage) message;
			String name = setNameMessage.getName();

			// Update local storage.
			this.players.updateName( id, name );

			// Send notification to all other Clients.
			List< IdTuple< String > > nameList = new ArrayList<>();
			nameList.add( new IdTuple<>( id, name ) );
			this.server.sendToAll( new NamesMessage( nameList ) );
		} else if ( message instanceof SetReadyMessage ) {
			SetReadyMessage setReadyMessage = (SetReadyMessage) message;
			boolean isReady = setReadyMessage.isReady();

			// Update local storage.
			this.players.updateReady( id, isReady );

			// Send notification to all other Clients.
			int noReady = 0;
			int noNotReady = 0;
			for ( Player player : this.players.getPlayers().values() ) {
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
}
