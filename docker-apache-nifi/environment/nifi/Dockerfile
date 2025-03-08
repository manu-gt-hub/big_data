FROM apache/nifi:latest

USER root

RUN apt-get update && apt-get -y install python3-pip

RUN pip3 install numpy scipy pandas matplotlib

COPY ./data/ /data/

COPY ./processing/ /processing/

RUN mkdir -p /data_formatted



