package glhf.common.entity.single;

import glhf.common.entity.SingleEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public class BooleanEntity extends SingleEntity< Boolean > {

	public BooleanEntity( boolean bool ) {
		super( bool );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeBoolean( this.object );
	}

}
