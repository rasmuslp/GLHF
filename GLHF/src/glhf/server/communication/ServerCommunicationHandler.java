package glhf.server.communication;

import glhf.common.player.Player;
import glhf.common.player.PlayerHandler;

public class ServerCommunicationHandler extends PlayerHandler< ServerCommunicationListener > {

	protected void notifyPlayerChat( Player sender, String chat ) {
		for ( ServerCommunicationListener listener : this.listeners ) {
			listener.playerChat( sender, chat );
		}
	}

	protected void notifyPlayerChatPrivate( Player sender, String chat, Player receiver ) {
		for ( ServerCommunicationListener listener : this.listeners ) {
			listener.playerChatPrivate( sender, chat, receiver );
		}
	}

	protected void notifyServerChat( String chat ) {
		for ( ServerCommunicationListener listener : this.listeners ) {
			listener.serverChat( chat );
		}
	}

	protected void notifyServerChatPrivate( String chat, Player receiver ) {
		for ( ServerCommunicationListener listener : this.listeners ) {
			listener.serverChatPrivate( chat, receiver );
		}
	}

}
