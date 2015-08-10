package br.com.aexo.atlas.application;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.aexo.atlas.domain.Acl;
import br.com.aexo.atlas.domain.AclRepository;
import br.com.aexo.atlas.domain.Balancer;
import br.com.aexo.atlas.domain.MarathonEventSubscriber;
import br.com.aexo.atlas.domain.Rule;
import br.com.aexo.atlas.domain.RuleRepository;
import br.com.aexo.atlas.domain.TemplateRepository;

@ApplicationScoped
public class AtlasService {

	@Inject
	private Balancer balancer;

	@Inject
	private AclRepository aclRepository;
	
	@Inject
	private TemplateRepository templateRepository;
	
	@Inject
	private MarathonEventSubscriber eventSubscriber;
	
	@Inject
	private RuleRepository ruleRepository;

	public AtlasService() {
	}

	public List<Acl> listAcls() {
		return aclRepository.listAcls();
	}

	public void saveAcl(Acl acl) {
		aclRepository.saveAcl(acl);
	}

	public void removeAcl(String appId) {
		aclRepository.removeAcl(appId);
	}

	public String getTemplate() {
		return templateRepository.getTemplate();
		
	}

	public void saveTemplate(String template) {
		templateRepository.saveTemplate(template);
	}

	public List<Rule> getRules() {
		return ruleRepository.getRules();
	}

	public synchronized void updateNotify() {
		String script = getTemplate();
		List<Acl> acls = listAcls();
		balancer.writeConfiguration(script, acls);
		balancer.reload();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}
	
	

	public String testScript(String script) {
		List<Acl> acls = listAcls();
		return balancer.testConfiguration(script, acls);
	}

	public void registryInMarathonEventsSubscriber() {
		eventSubscriber.registryInMarathonEventsSubscriber();
	}

	public void deRegistryInMarathonEventsSubscriber() {
		eventSubscriber.deRegistryInMarathonEventsSubscriber();
	}

}
