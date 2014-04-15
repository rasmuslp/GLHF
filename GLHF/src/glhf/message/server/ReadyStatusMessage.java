package glhf.message.server;

import glhf.message.IdTuple;
import glhf.message.ListMessage;
import glhf.message.MessageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crossnet.log.Log;
import crossnet.util.ByteArrayReader;
import crossnet.util.ByteArrayWriter;

public class ReadyStatusMessage extends ListMessage< IdTuple< Boolean > > {

	private final int noReady;
	private final int noNotReady;

	public ReadyStatusMessage( final int noReady, final int noNotReady, List< IdTuple< Boolean > > list ) {
		super( MessageType.S_READY_STATUS, list );
		this.noReady = noReady;
		this.noNotReady = noNotReady;
	}

	public int getNoReady() {
		return this.noReady;
	}

	public int getNoNotReady() {
		return this.noNotReady;
	}

	public boolean allReady() {
		if ( this.noNotReady == 0 ) {
			return true;
		}

		return false;
	}

	@Override
	protected void serializeStatic( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.noReady );
		to.writeInt( this.noNotReady );
	}

	@Override
	protected void serializeListObject( int atIndex, ByteArrayWriter to ) throws IOException {
		IdTuple< Boolean > idTuple = this.list.get( atIndex );
		to.writeInt( idTuple.getId() );
		to.writeBoolean( idTuple.getValue() );
	}

	/**
	 * Construct an ReadyStatusMessage from the provided payload.
	 * 
	 * @param payload
	 *            The payload from which to determine the content of this.
	 * @return A freshly parsed ReadyStatusMessage.
	 */
	public static ReadyStatusMessage parse( ByteArrayReader payload ) {
		try {
			int noReady = payload.readInt();
			int noNotReady = payload.readInt();
			int count = payload.readInt();
			List< IdTuple< Boolean > > readyIds = new ArrayList<>();
			for ( int i = 0; i < count; i++ ) {
				int id = payload.readInt();
				boolean ready = payload.readBoolean();
				readyIds.add( new IdTuple<>( id, ready ) );
			}
			return new ReadyStatusMessage( noReady, noNotReady, readyIds );
		} catch ( IOException e ) {
			System.out.println( "ROFL" );
			Log.error( "CrossNet", "Error deserializing ReadyStatusMessage:", e );
		}

		return null;
	}

}
