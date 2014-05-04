package glhf.common.entity.tuple;

import glhf.common.entity.IdTupleEntity;
import glhf.common.entity.single.BooleanEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public class IdBooleanEntity extends IdTupleEntity< BooleanEntity > {

	public IdBooleanEntity( final int id, final boolean bool ) {
		super( id, new BooleanEntity( bool ) );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
