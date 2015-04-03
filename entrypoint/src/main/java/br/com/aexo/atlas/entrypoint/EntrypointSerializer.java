package br.com.aexo.atlas.entrypoint;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.curator.framework.recipes.queue.QueueSerializer;

public class EntrypointSerializer implements QueueSerializer<EntrypointConfiguration>{

	@Override
	public EntrypointConfiguration deserialize(byte[] obj) {
		try {
			return (EntrypointConfiguration) new ObjectInputStream(new ByteArrayInputStream(obj))
			.readObject();
		} catch ( Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] serialize(EntrypointConfiguration arg0) {
		
		return null;
	}

}
