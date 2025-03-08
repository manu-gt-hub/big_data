#!/bin/bash
PROJECT_VERSION=0.1.0-SNAPSHOT

#hadoop
docker build -t insert_hadoop environment/hadoop/

echo "+++++++++++++++++++++++++++++++++++++++++++++"
echo "Insertion project Images are build succesfully!     "
echo "+++++++++++++++++++++++++++++++++++++++++++++"
exit 0
