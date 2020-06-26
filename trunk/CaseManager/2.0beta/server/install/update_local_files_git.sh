#!/bin/bash
### WEBSERVER LIBRARY
echo "	copying patho webapp library: webapp_server_lib-0.1.jar"
if [ ! -d ./modules/net/rehkind_mag/webapplib/main/ ]
then
	echo "	subdirectory for webapp library module does not exist, creating directory structure: ./modules/net/rehkind_mag/webapplib/main/"
	mkdir -p ./modules/net/rehkind_mag/webapplib/main/
fi

CMD="cp ${SRC_DIRECTORY}/../lib/target/webapp_server_lib-0.1.jar ./modules/net/rehkind_mag/webapplib/main/"
#echo "CMD=$CMD"
$CMD
### WEBSERVER
#echo "	copying patho server application: webapp_server-web-0.1.war"
if [ ! -d ./standalone/deployments/ ]
then
	echo "	subdirectory for wildfly server deployments does not exist, creating directory structure: ./standalone/deployments/"
	mkdir -p ./standalone/deployments/
fi
#CMD="cp ${SRC_DIRECTORY}/webapp_server-ejb/target/webapp_server-ejb-0.1.jar ./standalone/deployments/"
#echo "CMD=$CMD"
#$CMD

echo "	copying patho http server: webapp_server-web-0.1.war"
CMD="cp ${SRC_DIRECTORY}/webapp_server-web/target/webapp_server-web-0.1.war $(pwd)/standalone/deployments/"
echo "CMD=$CMD"
$CMD
