package glhf.common.entity.single;

import glhf.common.entity.SingleEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public class StringEntity extends SingleEntity< String > {

	public StringEntity( String string ) {
		super( string );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeString255( this.object );
	}

}
