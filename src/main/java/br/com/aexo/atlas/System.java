package br.com.aexo.atlas;

import br.com.aexo.atlas.web.rest.JaxRsActivator;

/**
 * Class used to describe the application of rest api swagger
 * 
 * @author carlosr
 *
 */
public class System  { 

	public final static String PATH_RESOURCES = "/rest";
	
	public String getTitle() {
		return "Atlas";
	}

	public String getDescription() {
		return "Load balancer manager";
	}

	public String getMailDeveloper() {
		return "carlos.alberto@aexo.com.br";
	}

	public String getUrlPathResources(){
		return PATH_RESOURCES;
	}
	
	public String getPackageRestResourcesBase() {
		return JaxRsActivator.class.getPackage().getName();
	}
}