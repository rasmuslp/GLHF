package glhf.common.player;

import glhf.server.GlhfServer;

/**
 * Notifies about {@link Player} changes.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public interface PlayerListener {

	/**
	 * New Player connected.
	 * 
	 * @param player
	 *            The new Player.
	 */
	public void connected( Player player );

	/**
	 * Player disconnected.
	 * 
	 * @param player
	 *            The Player that disconnected.
	 */
	public void disconnected( Player player );

	/**
	 * Player attributes changed.
	 * 
	 * @param player
	 *            The player which attributes changed.
	 */
	public void updated( Player player );

	/**
	 * Chat from sender to receiver.
	 * 
	 * @param sender
	 *            The sender of the message. Iff this is {@code null}, then it is from the {@link GlhfServer}.
	 * @param chat
	 *            The chat message.
	 * @param receiver
	 *            The recipient of the message. Iff this is {@code null}, then it is public.
	 */
	public void chat( Player sender, String chat, Player receiver );

}
