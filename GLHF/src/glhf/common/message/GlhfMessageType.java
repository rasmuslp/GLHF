package glhf.common.message;

import glhf.client.GlhfClient;

/**
 * The various types of {@link GlhfMessage}s.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public enum GlhfMessageType {

	// Server Messages

	S_IDS,

	S_CONNECTION_CHANGE,

	S_NAMES,

	S_PINGS,

	S_READYS,

	// Client Messages

	/**
	 * Sets the name for the {@link GlhfClient}.
	 */
	C_NAME,

	/**
	 * Sets the ready status for the {@link GlhfClient}.
	 */
	C_READY,

	// Common Messages

	CHAT,

	TIERED,

}
