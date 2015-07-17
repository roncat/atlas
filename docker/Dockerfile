FROM haproxy:1.5.12

MAINTAINER AEXO TI - <carlos.alberto@aexo.com.br>


RUN apt-get update && apt-get -y upgrade && apt-get install -y unzip curl

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


ENV NAMESPACE atlas
ENV ZK_SERVER localhost:2181 
ENV MARATHON_URL localhost:8080

ENV CONFIG_FILE_NAME "/etc/haproxy/haproxy.cfg"
ENV COMMAND /atlas/reload
ENV CALLBACK http://localhost:30000/atlas/rest/update-notify

RUN mkdir /atlas

WORKDIR /atlas


RUN curl -O http://download.jboss.org/wildfly/9.0.0.Final/wildfly-9.0.0.Final.zip  && unzip wildfly-9.0.0.Final.zip && rm wildfly-9.0.0.Final.zip

ADD https://github.com/aexoti/atlas/releases/download/atlas-0.29.6/atlas-0.29.6.war /atlas/wildfly-9.0.0.Final/standalone/deployments/atlas.war 

ADD start.sh /atlas/start.sh
ADD reload /atlas/reload
RUN ln -s /usr/local/etc/haproxy /etc/haproxy
RUN mkdir /var/lib/haproxy -p 
RUN mkdir /var/log/haproxy -p
ADD haproxy.cfg /etc/haproxy/haproxy.cfg

CMD /atlas/start.sh

