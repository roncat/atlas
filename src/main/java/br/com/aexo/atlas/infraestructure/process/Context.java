package br.com.aexo.atlas.infraestructure.process;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the data for the execution of a process
 * 
 * @author carlosr
 * 
 */
public class Context {

	Map<String, Object> contexto = new HashMap<String, Object>();

	/**
	 *stores information in context for the key
	 * 
	 * @param chave
	 * @param valor
	 */
	public void set(String chave, Object valor) {
		contexto.put(chave, valor);
	}

	/**
	 * retrieves context information over the last key
	 * 
	 * @param chave
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String chave) {
		return (T) contexto.get(chave);
	}

}