package br.com.aexo.atlas.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.WatchedEvent;
import org.junit.Test;

public class Zookeeper {

	public static void main(String[] args) throws Exception {

		TestingServer zk = new TestingServer();

		CuratorFramework client = CuratorFrameworkFactory.builder().retryPolicy(new ExponentialBackoffRetry(1000, 3)).connectString(zk.getConnectString()).namespace("atlas").build();
		client.start();
		
		client.create().forPath("/entrypoint");

		new AtlasMaster(client, "atlas 1", 3);
		new AtlasMaster(client, "atlas 2", 3);
		new AtlasMaster(client, "atlas 3", 3);
		new AtlasMaster(client, "atlas 4", 3);
		new AtlasMaster(client, "atlas 5", 3);

		CuratorWatcher w = new CuratorWatcher() {
			
			@Override
			public void process(WatchedEvent event) throws Exception {
				
				System.out.println("escrito no entrypoint "+new String(client.getData().forPath(event.getPath())));
				client.getData().usingWatcher(this).forPath("/entrypoint");
			}
		};
		
		client.getData().usingWatcher(w).forPath("/entrypoint");
		
	}
}
