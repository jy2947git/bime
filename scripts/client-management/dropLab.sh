#!/bin/bash
#
# the account to run this script must can write to the client home directory
# it assumes the dropDatabase.sql is already existing in the client home directory. this sql script
# should have been created when the client is created.
# this script will first run the dropDatabase.sql and then rename the client-directory to client-cancelled
#
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
