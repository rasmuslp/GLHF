package glhf.server.communication;

import glhf.common.player.Player;
import glhf.common.player.PlayerListener;

public interface ServerCommunicationListener extends PlayerListener {

	public void playerChat( Player sender, String chat );

	public void playerChatPrivate( Player sender, String chat, Player receiver );

	public void serverChat( String chat );

	public void serverChatPrivate( String chat, Player receiver );

}
