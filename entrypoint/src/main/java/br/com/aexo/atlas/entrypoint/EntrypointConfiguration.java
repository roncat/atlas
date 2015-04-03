package br.com.aexo.atlas.entrypoint;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntrypointConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	Map<Integer,Set<EndpointLoadBalancer>> config = new HashMap<>();
	
	
	public Set<Integer> ports(){
		return Collections.unmodifiableSet(config.keySet());
	}
	
	public Set<EndpointLoadBalancer> loadBalancersIn(Integer port){
		return config.get(port);
	}
	
}
