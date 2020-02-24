#!/bin/bash
SCRIPT_PATH=$(dirname $0)
SCRIPT_PATH=$(realpath $SCRIPT_PATH)
SERVER=$1

if [ -z $SERVER ]
then
	SERVER="test_server"
fi
LOCAL_PATH=$SCRIPT_PATH/$SERVER/

echo "Entering directory $SCRIPT_PATH/$SERVER"
cd $SCRIPT_PATH/$SERVER/
source ./settings.cnf

echo "@ENV SRC_DIRECTORY=$SRC_DIRECTORY"
echo "@ENV SERVER_ADDRESS=$SERVER_ADDRESS"
echo "@ENV SERVER_WILDFLY_PATH=$SERVER_WILDFLY_PATH"
echo "@ENV SERVER_SSH_USER=$SERVER_SSH_USER"

echo "[LOCAL UPDATE] Performing update of local files..."

	$SCRIPT_PATH/update_local_files.sh

	
### PUSH LOCAL FILES TO SERVER:
	echo "	Target address: $SERVER:$SERVER_WILDFLY_PATH"
	for FILE in *
	do
		### IGNORE LOCALE CONFIGURATION FILES ETC...IGNORING IS IGNORED
		if [ ! $FILE == "settings.cnf" ]
		then
			if [ -d $FILE ]
			then
				echo "		copying directory $FILE"
			else
				echo "		copying file $FILE"
			fi
		fi
	done
	
	CMD="scp -r $LOCAL_PATH/* $SERVER_SSH_USER@$SERVER_ADDRESS:$SERVER_WILDFLY_PATH"
	#echo "		CMD=$CMD"
	$CMD
echo "[LOCAL UPDATE] Files up to date."