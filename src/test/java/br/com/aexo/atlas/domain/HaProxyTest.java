package br.com.aexo.atlas.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class HaProxyTest {

	private HaProxy haproxy;
	private String configFileName;

	@Before
	public void setup() {
		configFileName = "/tmp/" + UUID.randomUUID();
		haproxy = new HaProxy(configFileName,"command for restart","marathonURL");
	}

	@Test
	public void shouldBeGetActualConfigurationInBalancerFile() throws Exception {
		
		writeConfig("Test");

		String actualConfiguration = haproxy.getActualConfiguration();
		assertThat(actualConfiguration, is("Test"));
	}

	private void writeConfig(String string) throws Exception {
		FileOutputStream out = new FileOutputStream(configFileName);
		out.write(string.getBytes());
		out.close();
	}

	@Test
	public void shoulBeExecuteCommandForRestartBalancer() throws Exception {
		
		// This test ONLY test if a file has been CREATED When running hum script in OS
		
		String script = "/tmp/" + UUID.randomUUID();
		String file = "/tmp/" + UUID.randomUUID().toString();
		write(script, "touch " + file);
		
		// configure correct command for test
		haproxy.command = "sh "+script;
		
		File fileToTestCommand = new File(file);

		if (fileToTestCommand.exists()) {
			fileToTestCommand.delete();
		}

		// execute command
		haproxy.reload();

		assertThat(fileToTestCommand.exists(), is(true));
	}

	private void write(String file, String content) throws Exception {
		FileOutputStream out = new FileOutputStream(file);
		out.write(content.getBytes());
		out.close();
	}

}
