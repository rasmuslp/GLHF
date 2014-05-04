package glhf.common.entity.single;

import glhf.common.entity.SingleEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public class IntegerEntity extends SingleEntity< Integer > {

	public IntegerEntity( Integer integer ) {
		super( integer );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		to.writeInt( this.object );
	}

}
