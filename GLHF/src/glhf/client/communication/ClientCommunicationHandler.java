package glhf.client.communication;

import glhf.common.player.Player;
import glhf.common.player.PlayerHandler;

public class ClientCommunicationHandler extends PlayerHandler< ClientCommunicationListener > {

	protected void notifyPlayerChat( Player sender, String chat ) {
		for ( ClientCommunicationListener listener : this.listeners ) {
			listener.playerChat( sender, chat );
		}
	}

	protected void notifyPlayerChatPrivate( Player sender, String chat ) {
		for ( ClientCommunicationListener listener : this.listeners ) {
			listener.playerChatPrivate( sender, chat );
		}
	}

	protected void notifyServerChat( String chat ) {
		for ( ClientCommunicationListener listener : this.listeners ) {
			listener.serverChat( chat );
		}
	}

	protected void notifyServerChatPrivate( String chat ) {
		for ( ClientCommunicationListener listener : this.listeners ) {
			listener.serverChatPrivate( chat );
		}
	}

}
