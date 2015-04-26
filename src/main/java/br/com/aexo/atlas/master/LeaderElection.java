package br.com.aexo.atlas.master;

import java.io.Closeable;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

public class LeaderElection extends LeaderSelectorListenerAdapter implements Closeable {

	private final LeaderSelector leaderSelector;

	private boolean leader;

	public LeaderElection(CuratorFramework client) {
		leaderSelector = new LeaderSelector(client, "/leader", this);
		leaderSelector.autoRequeue();
	}

	public void start() throws IOException {
		leaderSelector.start();
	}

	public synchronized boolean isLeader() {
		return leader;
	}

	private synchronized void setLeader(boolean leader) {
		this.leader = leader;
	}

	@Override
	public void close() throws IOException {
		leaderSelector.close();
	}

	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		System.out.println("take leader now");
		setLeader(true);
		try {
			while (true) {
				Thread.sleep(3000);
			}
		} finally {
			setLeader(false);
		}
	}
}