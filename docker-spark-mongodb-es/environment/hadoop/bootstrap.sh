#!/bin/bash
service ssh restart &
# Inicia servicios de hdfs
/opt/sds/hadoop/sbin/start-dfs.sh

# Datalake
year=$(date +"%Y")

hdfs dfs -mkdir -p /datalake

# Hdfs permisos
hdfs dfs -chmod -R 777 /datalake

# Proceso en background para evitar que el contenedor termine.
tail -f /dev/null
