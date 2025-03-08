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

ENV HADOOP_VERSION=2.7.5

RUN mkdir /opt/sds

# Download Hadoop and install it in /opt/sds/hadoop
RUN wget https://archive.apache.org/dist/hadoop/core/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz -O /opt/hadoop-${HADOOP_VERSION}.tar.gz
RUN cd /opt/ && tar xf hadoop-${HADOOP_VERSION}.tar.gz
RUN ln -s /opt/hadoop-${HADOOP_VERSION} /opt/sds/hadoop

ENV HADOOP_HOME /opt/sds/hadoop

# HDFS config in all nodes with HDFS
RUN mv /opt/sds/hadoop/etc/hadoop/core-site.xml /opt/sds/hadoop/etc/hadoop/core-site.xml.bak
RUN echo "<configuration><property><name>fs.defaultFS</name><value>hdfs://hadoop:8020</value></property></configuration>" > /opt/sds/hadoop/etc/hadoop/core-site.xml
RUN mv /opt/sds/hadoop/etc/hadoop/hdfs-site.xml /opt/sds/hadoop/etc/hadoop/hdfs-site.xml.bak
RUN echo "<configuration><property><name>dfs.replication</name><value>1</value></property><property><name>dfs.permissions</name><value>false</value></property></configuration>" > /opt/sds/hadoop/etc/hadoop/hdfs-site.xml

# Including Hadoop environment and HDFS commands into bash
ENV HADOOP_CONF_DIR /opt/sds/hadoop/etc/hadoop

ENV PATH $PATH:$HADOOP_HOME/bin

# SET Java environment (needed to run hadoop)
RUN sed -i '/export JAVA_HOME=\${JAVA_HOME}/c export JAVA_HOME=/usr/lib/jvm/java-8-oracle' ${HADOOP_CONF_DIR}/hadoop-env.sh

# format namenode
RUN /opt/sds/hadoop/bin/hdfs namenode -format

USER root

# Bootstrap script
ADD bootstrap.sh /etc/bootstrap.sh
RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

ENV BOOTSTRAP /etc/bootstrap.sh

ENTRYPOINT ["/etc/bootstrap.sh"]
