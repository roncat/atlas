package br.com.aexo.atlas.entrypoint;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;

public class EntrypointConsumer implements QueueConsumer<EntrypointConfiguration> {

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState state) {
		System.out.println("State [" + state + "]");
	}

	@Override
	public void consumeMessage(EntrypointConfiguration config) throws Exception {
		System.out.println("Consuming (" + config + ")");	
	}

}
