# atlas

[![Build Status](https://travis-ci.org/aexoti/atlas.svg)](https://travis-ci.org/aexoti/atlas)

Atlas is load balancer manager


o atlas é dividido em 2 componentes:

slave - responsável por executar a configuração passada e reiniciar o balancer 

master - responsável por prover a interface web administrativa, monitorar o marathon, e enviar as configurações para o slave.

No monitoramento, deve ser feita a eleição de um lider, entre os masters, e apenas o master lida com o marathon obtendo informações e enviando para o slave.

usando via docker

docker run -e ZK="zookeeper hosts" -e MARATHON_URL="host:port"  -e PORT="service port" -e HOSTNAME="ip to host"  -e CALLBACK="http://ip do host:port/update-notify" --net=host aexoti/atlas-master:<version>

docker run -e ZK="zookeeper hosts" -e PORT="service this port" --net=host -e HOSTNAME="ip do host" -e MARATHON_URL="host:port"  -v /run:/run -v /dev:/dev aexoti/atlas-slave:<version>

