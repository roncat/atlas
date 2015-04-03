package br.com.aexo.atlas.commons.templates;

import java.util.HashMap;

public class Context extends HashMap<String,Object> {
	
	private static final long serialVersionUID = 1L;

	public void set(String key,Object obj){
		this.put(key, obj);
	}
	
	public <T> T get(String key){
		return this.get(key);
 	}

}
