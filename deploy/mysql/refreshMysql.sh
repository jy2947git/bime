#!/bin/bash
cat ./dropDatabase.sql ./createDatabase.sql ./schema.sql ./initialize_system.sql ./initialize_lab.sql ./initialize_bime_lab.sql ./initialize_testing_data.sql>all.sql
mysql -u root -pG04theau < all.sql
rm all.sql