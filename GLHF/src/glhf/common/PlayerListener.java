package glhf.common;

public interface PlayerListener {

	public void connected( Player player );

	public void disconnected( Player player );

	public void playerUpdated( Player player );

	public void playerChat( Player sender, String chat );

	public void playerChatToPlayer( Player sender, String chat, Player receiver );

}
