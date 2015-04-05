package br.com.aexo.atlas.master.discovery;

import java.io.Closeable;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

public class AtlasServiceDiscovery extends LeaderSelectorListenerAdapter implements Closeable {

	private static String PATH_LEADER = "leader";

	private final String serverId;
	private final LeaderSelector leaderSelector;

	public AtlasServiceDiscovery(CuratorFramework client, String serverId) {
		this.serverId = serverId;
		leaderSelector = new LeaderSelector(client, PATH_LEADER, this);
		leaderSelector.autoRequeue();
		leaderSelector.start();

	}

	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		try {
			// agora eu sou o lider
			
			// buscar apps no marathon
			// buscar aplicações no servidor
			// registrar no marathon os endpoints do 
			//   descobrimento de servico
			// buscar servidores escravos
			// enviar nova configuração para os escravos
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		leaderSelector.close();
	}
}
