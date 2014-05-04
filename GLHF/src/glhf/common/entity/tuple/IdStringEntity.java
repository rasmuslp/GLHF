package glhf.common.entity.tuple;

import glhf.common.entity.IdTupleEntity;
import glhf.common.entity.single.StringEntity;

import java.io.IOException;

import crossnet.util.ByteArrayWriter;

public class IdStringEntity extends IdTupleEntity< StringEntity > {

	public IdStringEntity( final int id, String string ) {
		super( id, new StringEntity( string ) );
	}

	@Override
	public void serialise( ByteArrayWriter to ) throws IOException {
		this.id.serialise( to );
		this.entity.serialise( to );
	}

}
