package glhf.common.player;

public interface PlayerListener {

	public void connected( Player player );

	public void disconnected( Player player );

	public void playerUpdated( Player player );

}
