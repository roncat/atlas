package br.com.aexo.atlas.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HaProxy extends AbstractBalancer {

	protected String configFileName;
	protected String command;

	public HaProxy(String configFileName, String command,String marathonUrl) {
		super(marathonUrl);
		this.configFileName = configFileName;
		this.command = command;

	}

	public String getActualConfiguration() {
		try {
			return new String(Files.readAllBytes(Paths.get(configFileName)));
		} catch (IOException e) {
			// TODO tratar melhor exception
			throw new RuntimeException(e);
		}
	}

	public void reload() {
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (Exception e) {
			// TODO tratar melhor a exception
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeConfiguration(String script,List<Acl> acls) {
		String config = testConfiguration(script,acls);

		try {
			FileOutputStream out = new FileOutputStream(new File(configFileName));
			org.apache.commons.io.IOUtils.copy(new ByteArrayInputStream(config.getBytes()), out);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
