package br.com.aexo.atlas.domain;

import javax.inject.Inject;

import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class MarathonEventSubscriber {

	@Inject
	private AtlasConfiguration config;

	public void registryInMarathonEventsSubscriber() {
		try {

			Request.Post(config.getMarathonURL() //
					.concat("/v2/eventSubscriptions?callbackUrl=") //
					.concat(config.getCallback())) //
					.version(HttpVersion.HTTP_1_1) //
					.bodyString("", ContentType.APPLICATION_JSON)//
					.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deRegistryInMarathonEventsSubscriber() {
		try {

			Request.Delete(config.getMarathonURL() //
					.concat("/v2/eventSubscriptions?callbackUrl=") //
					.concat(config.getCallback())) //
					.version(HttpVersion.HTTP_1_1) //
					.execute();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
