package br.com.aexo.atlas;

import org.apache.curator.test.TestingServer;

import br.com.aexo.atlas.master.AtlasMaster;
import br.com.aexo.atlas.slave.AtlasSlave;

public class Test {

	public static void main(String[] args) throws Exception {
		
		TestingServer testingServer = new TestingServer();
		String zk = testingServer.getConnectString();
		
		AtlasMaster master = new AtlasMaster(zk, "172.19.160.111:8080", "localhost", 8081);
		AtlasSlave slave = new AtlasSlave(zk, "172.19.160.111:8080", "localhost", 8082, "target/tmp/?fileName=haproxy2.cfg", "touch?args=target/tmp/ha2");
		
		master.start();
		slave.start();
	}
	
}
