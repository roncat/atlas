# atlas

[![Build Status](https://travis-ci.org/aexoti/atlas.svg)](https://travis-ci.org/aexoti/atlas)

Atlas is a load balancer for marathon framework in mesos cluster, providing high availability for applications and managing rules of custom acl for each application fully dynamic way. 


## Funcionamento ##

O sistema atlas é dividido em 4 componentes:

- DiscoveryService - componente responsável por fazer descoberta dos serviços registrar a modificação no zookeeper e notifica da modificação para os demais componentes, este componente está embutido no componente web e participa de uma eleição de lideres pois devemos ter apenas um discovery no barramento fazendo o descobrimento.

- EntryPointBalancer - componente responsável por gerenciar um ha-proxy expondo as portas e redirecionando para os haproxys de aplicação (endpoint), este componente é responsável por gerenciar o ciclo de vida do balancer reiniciando o mesmo quando necessário e reconfigurando o balancer, este componente pode estar instalado em diversos servidores que poderam ser usados para fazer roundrobin a nivel de dns e assim garantir a alta disponibilidade dos servidores.

- EndPointBalancer - componente responsável por gerenciar diversos ha-proxy, iniciando um haproxy por aplicação, e aplicando a configuração especificada no template para balancear para os sistemas descobertos no componente discovery service, este componente pode estar instalado em diversos servidores com a finalidade de ter alta disponibilidade.

- web - componente responsável por ter uma interface amigavel para o sistema, permitindo gerenciar as regras de acl que devem ser aplicadas no entrypoint e no endpoint permitindo gerenciar os templates que devem ser aplicados, deve também disponibilizar uma api rest para permitir o uso de um agente externo configurar o sistema.

Todos os serviços devem disponibilizar uma api rest para efetuar os seus trabalhos, e a comunicação será efetuada pelo barramento do zookeeper.


