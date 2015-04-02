package br.com.aexo.atlas.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

public class AtlasMaster extends LeaderSelectorListenerAdapter implements Closeable {

	private static String PATH_LEADER = "/atlas/leader";

	private String nameNodeServer;
	private final LeaderSelector leaderSelector;

	private Integer waitTimeLeader;

	public AtlasMaster(CuratorFramework client, String nameNodeServer, Integer waitTimeLeader) {
		this.waitTimeLeader = waitTimeLeader;
		this.nameNodeServer = nameNodeServer;
		leaderSelector = new LeaderSelector(client, PATH_LEADER, this);
		leaderSelector.autoRequeue();
		leaderSelector.start();

	}

	public String getNameNodeServer() {
		return nameNodeServer;
	}

	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		try {
			client.setData().forPath("/entrypoint", nameNodeServer.getBytes());
			System.out.println(nameNodeServer + " is now the leader. Waiting " + waitTimeLeader + " seconds...");
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(waitTimeLeader));
			} catch (InterruptedException e) {
				System.err.println(nameNodeServer + " was interrupted.");
				Thread.currentThread().interrupt();
			} finally {
				System.out.println(nameNodeServer + " relinquishing leadership.\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		leaderSelector.close();
	}
}