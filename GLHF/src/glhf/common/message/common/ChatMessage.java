package glhf.common.message.common;

import glhf.common.message.GlhfMessage;
import glhf.common.message.GlhfMessageType;

import java.io.IOException;
import java.nio.charset.Charset;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class ChatMessage extends GlhfMessage {

	private int senderId;
	private final int receiverId;
	private final String chat;

	public ChatMessage( final int senderId, final String chatMessage ) {
		this( senderId, -1, chatMessage );
	}

	public ChatMessage( final int senderId, final int receiverId, final String chat ) {
		super( GlhfMessageType.CHAT );
		if ( chat == null ) {
			throw new IllegalArgumentException( "Chat cannot be null." );
		}
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.chat = chat;
	}

	public boolean isPrivate() {
		if ( this.receiverId == -1 ) {
			return false;
		}

		return true;
	}

	public boolean isServerMessage() {
		if ( this.senderId == -1 ) {
			return true;
		}

		return false;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public void setSenderId( int id ) {
		this.senderId = id;
	}

	public int getReceiverId() {
		return this.receiverId;
	}

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
			return new ChatMessage( senderId, receiverId, chat );
		} catch ( IOException e ) {
			Log.error( "GLHF", "Error deserializing ChatMessage:", e );
		}

		return null;
	}

}
