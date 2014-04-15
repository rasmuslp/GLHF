package glhf.message.common;

import glhf.message.GlhfMessage;
import glhf.message.MessageType;

import java.io.IOException;
import java.nio.charset.Charset;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class ChatMessage extends GlhfMessage {

	private final int senderId;
	private final int receiverId;
	private final String chatMessage;

	public ChatMessage( final int senderId, final int receiverId, final String chatMessage ) {
		super( MessageType.CHAT );
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.chatMessage = chatMessage;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public int getReceiverId() {
		return this.receiverId;
	}

	public String getChatMessage() {
		return this.chatMessage;
	}

	@Override
	protected void serializePayload( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.senderId );
		to.writeInt( this.receiverId );
		to.writeByteArray( this.chatMessage.getBytes( Charset.forName( "UTF-8" ) ) );
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
			String chatMessage = new String( data, Charset.forName( "UTF-8" ) );
			return new ChatMessage( senderId, receiverId, chatMessage );
		} catch ( IOException e ) {
			Log.error( "CrossNet", "Error deserializing ChatMessage:", e );
		}

		return null;
	}

}
