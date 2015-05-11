FROM debian:jessie

MAINTAINER AEXO TI - <carlos.alberto@aexo.com.br>

RUN apt-get update && apt-get -y upgrade && apt-get install -yq ha-proxy

RUN \
    echo "===> add webupd8 repository..."  && \
    echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list  && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list  && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886  && \
    apt-get update


RUN echo "===> install Java"  && \
    echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections  && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections  && \
    DEBIAN_FRONTEND=noninteractive  apt-get install -y --force-yes oracle-java8-installer oracle-java8-set-default


ENV TYPE server
ENV NAMESPACE atlas
ENV HOSTNAME 0.0.0.0
ENV MASTER_PORT 3500
ENV SLAVE_PORT 3501
ENV CALLBACK ""

ENV CONF_BALANCER_DEST /etc/haproxy/haproxy.cfg
ENV COMMAND "haproxy -f /etc/haproxy.cfg -p /var/run/haproxy.pid -sf $(cat /var/run/haproxy.pid)"

ENTRYPOINT start.sh