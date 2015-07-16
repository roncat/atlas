package br.com.aexo.atlas.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;

import br.com.aexo.atlas.infraestructure.util.EncodingUtil;

public class AclRepository {
	@Inject
	private CuratorFramework client;

	public List<Acl> listAcls() {
		List<Acl> acls = new ArrayList<>();

		List<String> list;
		try {
			list = client.getChildren().forPath("/acls");
			for (String appId : list) {
				Acl acl = new Acl();
				acl.setAppId(java.net.URLDecoder.decode(appId, "ISO-8859-1"));
				acl.setAcl(new String(client.getData().forPath("/acls/" + appId)));
				acls.add(acl);
			}
		} catch (Exception e) {
			// TODO melhorar a implementação do tratamento de exceptions
			throw new RuntimeException(e);
		}
		return acls;
	}

	public void saveAcl(Acl acl) {
		String appId = "/acls/" + EncodingUtil.encodeURIComponent(acl.getAppId());
		String rule = acl.getAcl();

		try {
			if (client.checkExists().forPath(appId) == null) {
				client.create().forPath(appId, rule.getBytes());
			} else {
				client.setData().forPath(appId, rule.getBytes());
			}
		} catch (Exception e) {

			// TODO melhorar a implementação do tratamento de exceptions
			throw new RuntimeException(e);
		}
	}

	public void removeAcl(String appId) {
		appId = "/acls/" + EncodingUtil.encodeURIComponent(appId);

		try {
			if (client.checkExists().forPath(appId) != null) {
				client.delete().forPath(appId);
			}
		} catch (Exception e) {
			// TODO melhorar a implementação do tratamento de exceptions
			throw new RuntimeException(e);
		}
	}
}
