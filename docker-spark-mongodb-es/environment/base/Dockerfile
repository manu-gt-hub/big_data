#install from ubuntu
FROM jared314/docker-ubuntu-14-04-java8

# wait for it script
ADD common/wait-for-it.sh /opt/sbd/scripts/wait-for-it.sh
RUN chmod 700 /opt/sbd/scripts/wait-for-it.sh

# Setting debconf to non interactive
RUN echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections
RUN echo "deb http://security.ubuntu.com/ubuntu trusty-security main" >>  /etc/apt/sources.list

# Install some software
RUN sudo apt-get -y update
RUN sudo apt-get -yq install software-properties-common
RUN sudo apt-get -yq install git

# Install and configure SSH server (needed by hadoop)
RUN sudo apt-get -y install openssh-server
RUN echo "root:sbd" | sudo chpasswd
RUN sed -i "/PermitRootLogin without-password/s/^/#/" /etc/ssh/sshd_config
RUN echo "PermitRootLogin yes" >>  /etc/ssh/sshd_config
RUN ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
RUN cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
RUN chmod 0600 ~/.ssh/authorized_keys
RUN echo "    StrictHostKeyChecking no" >> /etc/ssh/ssh_config

# curl
RUN apt-get -y install curl

# SET JAVA_HOME (needed to run hadoop, among others)
ENV JAVA_HOME=/usr/lib/jvm/java-8-oracle/

#Cleansing
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
