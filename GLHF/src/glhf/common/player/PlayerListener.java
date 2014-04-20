package glhf.common.player;

public interface PlayerListener {

	public void connected( Player player );

	public void disconnected( Player player );

	public void updated( Player player );

	/**
	 * Chat from sender to receiver.
	 * 
	 * @param sender
	 *            The sender of the message. Iff this is {@code null}, then it is from the {@link Server}..
	 * @param chat
	 *            The chat message.
	 * @param receiver
	 *            The recipient of the message. Iff this is {@code null}, then it is public.
	 */
	public void chat( Player sender, String chat, Player receiver );

}
