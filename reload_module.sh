#!/bin/bash
cd /home/thibaut/Documents/M2/TER/datalift-sdmxdatacube/sdmxdatacube
ant
cp dist/sdmxdatacube.jar $DATALIFT_HOME/modules/
/home/thibaut/Documents/M2/TER/apache-tomcat-7.0.35/bin/shutdown.sh
/home/thibaut/Documents/M2/TER/apache-tomcat-7.0.35/bin/startup.sh
