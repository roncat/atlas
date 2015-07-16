package br.com.aexo.atlas.infraestructure.cdi;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.core.Application;

import org.reflections.Reflections;

import scala.collection.immutable.List;
import scala.collection.immutable.Nil$;
import br.com.aexo.atlas.System;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.reader.ClassReaders;

/**
 * Enables swagger scanning classpath for api annotation
 * 
 * @author carlosr
 *
 */
@WebListener
public class SwaggerConfigListener implements ServletContextListener {

	@Inject
	private System system;

	public void contextInitialized(ServletContextEvent event) {

		SwaggerConfig config = ConfigFactory.config();
		
		ServletContext application = event.getServletContext();
		InputStream inputStream = application.getResourceAsStream("/META-INF/MANIFEST.MF");
		Manifest manifest;
		String version;
		try {
			manifest = new Manifest(inputStream);
		
		version =  manifest.getMainAttributes().getValue("Specification-Version");
		} catch (IOException e) {
			throw new RuntimeException("cannot read manifest",e);
		}
		
		config.setApiVersion(version);
		config.setBasePath(event.getServletContext().getContextPath()
				+ system.getUrlPathResources());

		String title = system.getTitle() + " - Version: "
				+ version;
		String description = system.getDescription();
		String termsOfServiceUrl = null;
		String contact = system.getMailDeveloper();
		String license = null;
		String licenseUrl = null;
		ApiInfo apiInfo = new ApiInfo(title, description, termsOfServiceUrl,
				contact, license, licenseUrl);
		config.setApiInfo(apiInfo);

		ClassReaders.setReader(new DefaultJaxrsApiReader());
		ScannerFactory.setScanner(new DefaultJaxrsScanner() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			public List classesFromContext(Application app, ServletConfig sc) {
				List apiResources;
				Reflections reflections = new Reflections(system
						.getPackageRestResourcesBase());
				apiResources = Nil$.MODULE$;
				for (Class clazz : reflections.getTypesAnnotatedWith(Api.class)) {
					apiResources = apiResources.$colon$colon(clazz);
				}
				return apiResources;
			}

		});

	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}