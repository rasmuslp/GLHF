package glhf.common.message;


/**
 * The various types of {@link GlhfMessage}s.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public enum GlhfMessageType {

	// Common

	CHAT,

	DATA,

	// Server Messages

	S_IDS,

	S_CONNECTION_CHANGE,

	S_NAMES,

	S_PINGS,

	S_READYS,

	// Client Messages

	C_NAME,

	C_READY,

}
