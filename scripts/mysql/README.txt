this directory contains scripts and templates for multiple purposes:

1, schema.sql
this is a file generated automatically by running the Ant script /tools/build_hibernate_tool.xml
it is the DDL of the bime database. In development process, every time there is change to the data model
this ant script need to be ran to generate DDL and also update the local "bime" database.

This sql file later will be used to create database for each new client (lab)

2, initialize_system.sql
this script has the SQL statements to populate user role table.
It is used to populdate the bime database for local development and new clients

3, refreshMySql.sh
this shellscript is used by the /automation/deploy.sh to refresh database with latest DDL

4, jdbc.properties.template, initialize_lab.template, dropDatabase.template, createDatabase.template, auto_increment.template
those template files are used by the Cloud-Management Python module to generate SQL files for new clients

5, createDatabase.sql, dropDatabase.sql, initiliaze_bime_lab.sql, initialize_lab.sql, initialize_system,sql
those sql files are to create and populate the local "bime" database