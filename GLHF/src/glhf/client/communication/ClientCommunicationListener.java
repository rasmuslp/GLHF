package glhf.client.communication;

import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

public interface ClientCommunicationListener extends PlayerListener {

	public void playerChat( Player sender, String chat );

	public void playerChatPrivate( Player sender, String chat );

	public void serverChat( String chat );

	public void serverChatPrivate( String chat );

}
