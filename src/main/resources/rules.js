function processRules(marathonURL,AtlasService) {
	var HttpCall = Java.type('br.com.aexo.atlas.infraestructure.util.HttpCall');
	var Rule = Java.type('br.com.aexo.atlas.domain.Rule');

	var tasks = HttpCall.getJSON(marathonURL + "v2/tasks");
	var apps = HttpCall.getJSON(marathonURL + "v2/apps");

	var acls = AtlasService.listAcls();

	var marathonTasks = JSON.parse(tasks);
	var marathonApps = JSON.parse(apps);
	//var acls = JSON.parse(acls);

	var apps = marathonApps.apps;
	var tasks = marathonTasks.tasks;

	var tmp = {};

	for (x in apps) {
		tmp[apps[x].id] = new Rule(apps[x].id, '', '-', 'marathon');

	}

	for (x in acls) {
		var app = new Rule(acls[x].appId, acls[x].acl, '-', 'marathon');

		if (tmp[acls[x].appId] == undefined) {
			app.discovery = 'missing';
		}

		tmp[acls[x].appId] = app;
	}

	for (x in tasks) {
		if (tmp[tasks[x].appId].instances == '-') {
			tmp[tasks[x].appId].instances = "0";
		}
		var qtd = parseInt(tmp[tasks[x].appId].instances);
		qtd++;
		tmp[tasks[x].appId].instances =  qtd;
	}

	rules = new java.util.ArrayList();
	for (x in tmp) {
		rules.add(tmp[x]);
	}

	return rules;
}