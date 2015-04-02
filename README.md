# atlas
Atlas is a load balancer for marathon framework in mesos cluster, providing high availability for applications and managing rules of custom acl for each application fully dynamic way.


## Funcionamento ##

usar um frontend para ser a porta de entrada do sistema, se o sistema precisa da porta x aberta neste haproxy será aberta apenas essa porta,

e teremos o backend permitindo que sejam feitas configurações exclusivas para o balanceamento de cada sistema no marathon,

HAproxy --- primeira camada (entrypoint)

outro lb ---- endpoint balancer app (multiinstancias para alta disponibilidade?)

apps marathon  --- 3 nivel 




