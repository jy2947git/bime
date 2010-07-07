#!/bin/bash
#
# the account to run this script must can write to the client home directory
# it assumes the dropDatabase.sql is already existing in the client home directory. this sql script
# should have been created when the client is created.
# this script will first run the dropDatabase.sql and then rename the client-directory to client-cancelled
#
# This script is meant to be executed by the Cloud-Management Python module on
# the Bime instance. The CM module listens to RabbitMQ and run this script to drop the MySql database 
# of the new client (lab)
#
# It assumes to be executed within the client directory
#

if [ $# -eq 0 ]
then
echo "usage:dropLab.sh client-home-directory"
exit 0
fi
cd $1
mysql -u root -pG04theau < dropDatabase.sql
cd ..
rm -r $1 $1_cancelled

exit 1
