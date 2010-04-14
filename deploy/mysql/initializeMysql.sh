#!/bin/bash
cat ./createDatabase.sql ./chema.ddl ./initialize_system.sql ./initialize_lab.sql>all.sql
mysql -u root -pG04theau < all.sql
rm all.sql