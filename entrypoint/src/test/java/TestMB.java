import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.Time;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;


public class TestMB {

//	
//	@Test
//	public void test() throws Exception {
//		TestingServer zk = new TestingServer();
//
//		
//		CuratorFramework client = CuratorFrameworkFactory.builder().retryPolicy(new ExponentialBackoffRetry(1000, 3))
//				.connectString(zk.getConnectString()).namespace("atlas").build();
//		client.start();
//		
//		KafkaServer
		
		
		
		
//	}
	
	  private int brokerId = 0;
	    private String topic = "test";
	 
	    @Test
	    public void producerTest() throws InterruptedException {
	 
	        // setup Zookeeper
	        String zkConnect = TestZKUtils.zookeeperConnect();
	        EmbeddedZookeeper zkServer = new EmbeddedZookeeper(zkConnect);
	        ZkClient zkClient = new ZkClient(zkServer.connectString(), 30000, 30000, ZKStringSerializer$.MODULE$);
	 
	        // setup Broker
	        int port = TestUtils.choosePort();
	        Properties props = TestUtils.createBrokerConfig(brokerId, port);
	 
	        KafkaConfig config = new KafkaConfig(props);
	        Time mock = new Time();
	        KafkaServer kafkaServer = TestUtils.createServer(config, mock);
	 
	        // create topic
	        CreateTopicCommand.createTopic(zkClient, topic, 1, 1, "");
	 
	        List<KafkaServer> servers = new ArrayList<KafkaServer>();
	        servers.add(kafkaServer);
	        TestUtils.waitUntilMetadataIsPropagated(scala.collection.JavaConversions.asBuffer(servers), topic, 0, 5000);
	 
	        // setup producer
	        Properties properties = TestUtils.getProducerConfig("localhost:" + port, "kafka.producer.DefaultPartitioner");
	 
	        ProducerConfig pConfig = new ProducerConfig(properties);
	        Producer producer = new Producer(pConfig);
	 
	        // send message
	        KeyedMessage<Integer, String> data = new KeyedMessage(topic, "test-message");
	 
	        List<KeyedMessage> messages = new ArrayList<KeyedMessage>();
	        messages.add(data);
	 
	        producer.send(scala.collection.JavaConversions.asBuffer(messages));
	 
	        // cleanup
	        producer.close();
	        kafkaServer.shutdown();
	        zkClient.close();
	        zkServer.shutdown();
	    }

	
}
