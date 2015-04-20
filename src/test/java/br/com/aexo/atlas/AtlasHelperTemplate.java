package br.com.aexo.atlas;

import java.util.List;
import java.util.Map;

public interface AtlasHelperTemplate {

	List<Integer> getPortsMapping();
	Map<String,String> getAcls(Integer port);
	List<Instance> getServersFrom(String app,Integer port);
	
}
