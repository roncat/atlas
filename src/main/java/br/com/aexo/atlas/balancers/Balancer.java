package br.com.aexo.atlas.balancers;

import br.com.aexo.atlas.applications.Application;

/**
 * Representa o balancer (aplicação bigip, haproxy, nginx ou qualquer outra tecnologia de balancer)
 * 
 * @author Carlos Alberto
 *
 */
public interface Balancer {

	/**
	 * solicita a configuração do balancer
	 * 
	 * @param app
	 */
	void configure(Application app);
	
}
