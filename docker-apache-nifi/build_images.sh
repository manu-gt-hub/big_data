#!/bin/bash
PROJECT_VERSION=0.1.0-SNAPSHOT

#hadoop
docker build -t nifi_image environment/nifi/

echo "+++++++++++++++++++++++++++++++++++++++++++++"
echo "Nifi project Images are build succesfully!     "
echo "+++++++++++++++++++++++++++++++++++++++++++++"
exit 0
