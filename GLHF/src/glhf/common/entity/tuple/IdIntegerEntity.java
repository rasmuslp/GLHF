package glhf.common.entity.tuple;

import glhf.common.entity.IdTupleEntity;
import glhf.common.entity.single.IntegerEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public class IdIntegerEntity extends IdTupleEntity< IntegerEntity > {

	public IdIntegerEntity( final int id, final int integer ) {
		super( id, new IntegerEntity( integer ) );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
