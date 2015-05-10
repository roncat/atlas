FROM debian:jessie

MAINTAINER AEXO TI - <carlos.alberto@aexo.com.br>

RUN apt-get update && apt-get -y upgrade

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


RUN apt-get install -yq git maven

ENV HOME /root

# Define working directory.
WORKDIR /tmp

RUN git clone https://github.com/aexoti/atlas.git
WORKDIR /tmp/atlas

RUN mvn clean package
RUN mv target/atlas*-jar-with-dependencies.jar /opt/atlas.jar



