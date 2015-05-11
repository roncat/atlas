package br.com.aexo.atlas;

import org.apache.curator.test.TestingServer;

import br.com.aexo.atlas.master.AtlasMaster;
import br.com.aexo.atlas.slave.AtlasSlave;

public class Test {

	public static void main(String[] args) throws Exception {

		TestingServer testingServer = new TestingServer();
		String zk = testingServer.getConnectString();

		AtlasMaster master = new AtlasMaster(zk, "atlas","172.19.160.111:8080", "0.0.0.0", 8081,"http://localhost:8081/update-notify");
		AtlasSlave slave = new AtlasSlave(zk, "atlas","172.19.160.111:8080", "localhost", 8082, "target/tmp/?fileName=haproxy2.cfg", "touch?args=target/tmp/ha2");
//		FakeMarathon marathon = new FakeMarathon("localhost", 8283);
		
//		marathon.start();
		master.start();
		slave.start();
	}

}
