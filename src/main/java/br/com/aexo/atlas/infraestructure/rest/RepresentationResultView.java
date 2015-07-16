package br.com.aexo.atlas.infraestructure.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/**
 * classe responsável por configurar a exibição de uma representação no resultado
 * 
 * @author carlosr
 *
 */
public class RepresentationResultView implements ResultView<RepresentationResultView> {

	@Inject
	private HttpServletRequest request;

	@Inject
	private Providers providers;
	
	private Object from;
	private List<String> includes = new ArrayList<String>();
	private List<String> excludes = new ArrayList<String>();

	public RepresentationResultView include(String... paths) {
		this.includes.addAll(Arrays.asList(paths));
		return this;
	}

	public RepresentationResultView exclude(String... paths) {
		this.excludes.addAll(Arrays.asList(paths));
		return this;
	}

	public Response serialize() {
		final Object from = this.from;
		final String accept = request.getHeader("Accept") == null ? "application/json" : request.getHeader("Accept");
		final String contentType = request.getHeader("Content-Type") ==null ? "application/json" : request.getHeader("Content-Type");
		
		// efetua a escrita de forma performatica sem alto consumo de memoria
		StreamingOutput streamingOutput = new StreamingOutput() {

			@Override
			public void write(OutputStream out) throws IOException,	WebApplicationException {
				MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
				
				if (accept.toLowerCase().contains("application/xml")){
					mediaType = MediaType.APPLICATION_XML_TYPE;
				}
				
				ObjectMapper json = null;
				XmlMapper xml = null;
				
				ContextResolver<ObjectMapper> jsonResolver = providers.getContextResolver(ObjectMapper.class, mediaType);
				
				if (jsonResolver!=null) {
					json= jsonResolver.getContext(from.getClass());
				}
				
				ContextResolver<XmlMapper> xmlResolver = providers.getContextResolver(XmlMapper.class, mediaType);
				if (xmlResolver!=null) {
					xml = xmlResolver.getContext(from.getClass());
				}
				
				ObjectMapper serializer =mediaType.equals(MediaType.APPLICATION_JSON_TYPE) ? json : xml;
				
				
				serializer.addMixInAnnotations(Object.class, PropertyFilterMixIn.class);
				
				FilterProvider filters =  new SimpleFilterProvider().addFilter("filter properties by name", new IncludeExcludeFieldFilter(includes.toArray(new String[]{}), excludes.toArray(new String[]{})));
				serializer.writer(filters).writeValue(out, from);
			}
		};
		
		return Response.ok(streamingOutput).header("Accept", accept).header("Content-Type", contentType).build();
	}

	public RepresentationResultView from(Object from) {
		this.from = from;
		return this;
	}
	

}
