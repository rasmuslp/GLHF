package glhf.server;

import glhf.common.Player;
import glhf.common.PlayerListener;

public interface ServerPlayerListener extends PlayerListener {

	public void playerChat( Player sender, String chat );

	public void playerChatPrivate( Player sender, String chat, Player receiver );

	public void serverChat( String chat );

	public void serverChatPrivate( String chat, Player receiver );

}
