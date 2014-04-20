package glhf.client;

import glhf.common.Player;
import glhf.common.PlayerListener;

public interface ClientPlayerListener extends PlayerListener {

	public void playerChat( Player sender, String chat );

	public void playerChatPrivate( Player sender, String chat );

	public void serverChat( String chat );

	public void serverChatPrivate( String chat );

}
