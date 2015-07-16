package br.com.aexo.atlas.infraestructure.rest;

import javax.inject.Named;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ConfigSerialization {
	
	@Named("json")
	@Produces
	public ObjectMapper getJsonMapper() {
		return null;
	}

	@Named("xml")
	@Produces
	public XmlMapper getXmlMapper() {

		return null;
	}
}
