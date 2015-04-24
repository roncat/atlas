
marathonTasks = JSON.parse(tasks);
marathonApps = JSON.parse(apps);
acls = JSON.parse(acls);



apps = marathonApps.apps;
tasks = marathonTasks.tasks;
    

tmp = {};

for (x in apps){
    tmp[apps[x].id] = { appId:apps[x].id, acl : '', instances: '-', discovery: 'marathon' };
}

for (x in acls){
    var app = { appId:acls[x].appId, acl : acls[x].acl, instances: '-', discovery: 'marathon' };
    
    if (tmp[acls[x].appId]==undefined){
        app.discovery = 'missing';
    }
    
	tmp[acls[x].appId] = app;
}

for (x in tasks){
	if (tmp[tasks[x].appId].instances =='-'){
		tmp[tasks[x].appId].instances = 0;
	}
	tmp[tasks[x].appId].instances += 1;
}

rules = [];
for (x in tmp){
	rules.push(tmp[x]);
}

saida.println(JSON.stringify(rules));