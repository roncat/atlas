# atlas

[![Build Status](https://travis-ci.org/aexoti/atlas.svg)](https://travis-ci.org/aexoti/atlas)

Atlas is load balancer manager


o atlas é dividido em 2 componentes:

slave - responsável por executar a configuração passada e reiniciar o balancer 

master - responsável por prover a interface web administrativa, monitorar o marathon, e enviar as configurações para o slave.

No monitoramento, deve ser feita a eleição de um lider, entre os masters, e apenas o master lida com o marathon obtendo informações e enviando para o slave.




