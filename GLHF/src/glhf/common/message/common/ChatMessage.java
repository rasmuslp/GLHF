package glhf.common.message.common;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;
import glhf.server.ServerConnectionHandler;

import java.io.IOException;
import java.nio.charset.Charset;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

/**
 * The ChatMessage is for universal chat. It supports private messages; i.e. messages with a specified receiver. And it
 * supports server messages; i.e. messages without a specified sender.
 * <p>
 * NB: The {@link ServerConnectionHandler}, part of the {@link GlhfServer}, will fill in the {@link #senderId} for messages
 * from {@link GlhfClient}s automatically.
 * 
 * @author Rasmus Ljungmann Pedersen <rasmuslp@gmail.com>
 * 
 */
public class ChatMessage extends GlhfMessage {

	private int senderId;
	private final int receiverId;
	private final String chat;

	/**
	 * Create a public chat message.
	 * 
	 * @param chat
	 *            The message.
	 */
	public ChatMessage( final String chat ) {
		this( -1, chat, -1 );
	}

	/**
	 * Create a private chat message.
	 * 
	 * @param chat
	 *            The message.
	 * @param receiverId
	 *            The ID of the receiver.
	 */
	public ChatMessage( final String chat, final int receiverId ) {
		this( -1, chat, receiverId );
	}

	/**
	 * Creates a chat message.
	 * 
	 * @param senderId
	 *            The ID of the sender.
	 * @param chat
	 *            The message.
	 * @param receiverId
	 *            The ID of the receiver.
	 */
	private ChatMessage( final int senderId, final String chat, final int receiverId ) {
		super( GlhfMessageType.CHAT );
		if ( chat == null ) {
			throw new IllegalArgumentException( "Chat cannot be null." );
		}
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.chat = chat;
	}

	/**
	 * @return {@code True} iff private, false otherwise.
	 */
	public boolean isPrivate() {
		if ( this.receiverId == -1 ) {
			return false;
		}

		return true;
	}

	/**
	 * @return {@code True} iff chat message from server, false otherwise.
	 */
	public boolean isServerMessage() {
		if ( this.senderId == -1 ) {
			return true;
		}

		return false;
	}

	/**
	 * @return The ID of the sender.
	 */
	public int getSenderId() {
		return this.senderId;
	}

	/**
	 * Sets the sender ID.
	 * <p>
	 * This is used by the {@link GlhfServer} when it received a ChatMessage.
	 * 
	 * @param id
	 *            The ID of the sender.
	 */
	public void setSenderId( int id ) {
		this.senderId = id;
	}

	/**
	 * @return The ID of the receiver.
	 */
	public int getReceiverId() {
		return this.receiverId;
	}

	/**
	 * @return The chat message.
	 */
	public String getChat() {
		return this.chat;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.senderId );
		to.writeInt( this.receiverId );
		to.writeByteArray( this.chat.getBytes( Charset.forName( "UTF-8" ) ) );
	}

	/**
	 * Construct an ChatMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed ChatMessage.
	 */
	public static ChatMessage parse( ByteArrayReader payload ) {
		try {
			int senderId = payload.readInt();
			int receiverId = payload.readInt();
			int bytes = payload.bytesAvailable();
			byte[] data = new byte[bytes];
			payload.readByteArray( data );
			String chat = new String( data, Charset.forName( "UTF-8" ) );
			return new ChatMessage( senderId, chat, receiverId );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing ChatMessage:", e );
		}

		return null;
	}

}
