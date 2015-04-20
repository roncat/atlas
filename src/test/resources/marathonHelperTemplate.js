marathonApps = JSON.parse(body.getMarathonApps());
marathonTasks = JSON.parse(body.getMarathonTasks());

apps = {};
for (index in marathonApps.apps) {
	apps[marathonApps.apps[index].id] = marathonApps.apps[index];
}

// função usada para recuperar as acls de uma aplicação
function getAcls(port) {

	var acls = {};

	for (index in marathonApps.apps) {
		app = marathonApps.apps[index];
		portsMapping = app.container.docker.portMappings;
		for (i in portsMapping) {
			portMapping = portsMapping[i];
			if (port == portMapping.containerPort) {
				acls[app.id] = "path_beg " + app.id;
			}
		}
	}
	return acls;
}

// função usada para recuperar as instancias de um server em uma determinada porta
function getServersFrom(app, port) {
	var srvs = [];

	for (index in marathonTasks.tasks) {
		task = marathonTasks.tasks[index];
		srv = {};

		portIndex = 0;

		for (x in apps[task.appId].container.docker.portMappings) {
			if (apps[task.appId].container.docker.portMappings[x].containerPort == port) {
				portIndex = x;
			}
		}

		if (task.appId == app) {
			srv["host"] = task.host;
			srv["port"] = task.ports[portIndex];
			srv["alive"] = true;
			for (i in task.healthCheckResults) {
				if (!task.healthCheckResults[i].alive) {
					srv["alive"] = false;
				}
			}
			srvs.push(srv);
		}
	}
	return srvs;
}

// função usada para recuperar as portas mapeadas para as instancias
function getPortsMapping() {
	var ports = [];
	for (index in marathonApps.apps) {
		app = marathonApps.apps[index];
		portsMapping = app.container.docker.portMappings;
		for (i in portsMapping) {
			portMapping = portsMapping[i];
			ports.push(portMapping.containerPort);
		}
	}
	return uniq(ports);
}

// função para eliminar duplicados em uma array
function uniq(a) {
	var seen = {};
	var out = [];
	var len = a.length;
	var j = 0;
	for (var i = 0; i < len; i++) {
		var item = a[i];
		if (seen[item] !== 1) {
			seen[item] = 1;
			out[j++] = item;
		}
	}
	return out;
}

// implementa uma interface java para ser usada no velocity

load("nashorn:mozilla_compat.js");
var helper = Java.type('br.com.aexo.atlas.AtlasHelperTemplate');
var helperImpl = Java.extend(helper, {
	getPortsMapping : getPortsMapping,
	getAcls : getAcls,
	getServersFrom : getServersFrom
});

// configura o helper como body para prosseguir processamento no template
body = new helperImpl();
