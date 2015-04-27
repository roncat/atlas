MAINTAINER AEXO TI

FROM debian:jessie

RUN apt-get update && apt-get -y upgrade
RUN apt-get install -y software-properties-common byobu curl git htop man unzip vim wget

RUN apt-get install -y openjdk-8-jdk

ENV HOME /root

# Define working directory.
WORKDIR /root
