package br.com.aexo.atlas.infraestructure.rest;

import javax.enterprise.inject.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Provider
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class DefaultJacksonJsonProvider extends JacksonJsonProvider implements
		MessageBodyReader<Object>, MessageBodyWriter<Object>, Versioned {

	@Produces
	public Providers producesProvides(){
		return _providers;
	}
	
	@Override
	public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
		ObjectMapper m = null;
		ObjectMapper json = null;
		XmlMapper xml = null;
		
		ContextResolver<ObjectMapper> jsonResolver = _providers.getContextResolver(ObjectMapper.class, mediaType);
		
		if (jsonResolver!=null) {
			json= jsonResolver.getContext(type);
		}
		
		ContextResolver<XmlMapper> xmlResolver = _providers.getContextResolver(XmlMapper.class, mediaType);
		if (xmlResolver!=null) {
			xml = xmlResolver.getContext(type);
		}
		
		
		
		m = mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE) ? json : xml;
		if (m == null) {
			m = _mapperConfig.getDefaultMapper();
		}
		return m;
	}

}
