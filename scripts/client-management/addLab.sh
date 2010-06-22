#!/bin/bash
#
# the account to run this script must can write to the client home directory
# script to create a new client lab.
# dropDatabase.sql, createDatabase.sql and initialize_lab.sql must be
# existing in the lab directory
# jdbc.properties must be existing in lab directory too.
#
if [ $# -eq 0 ]
then
echo "usage:addLab.sh client-home-directory"
exit 0
fi
cp /usr/local/bime-home/bime/scripts/mysql/schema.sql $1
cp /usr/local/bime-home/bime/scripts/mysql/initialize_system.sql $1
cd $1
cat ./dropDatabase.sql ./createDatabase.sql ./schema.sql ./auto_increment.sql ./initialize_system.sql ./initialize_lab.sql>all.sql
mysql -u root -pG04theau < all.sql
#rm all.sql

exit 1
