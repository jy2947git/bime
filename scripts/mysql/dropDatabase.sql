drop database if exists bime ;
grant usage on *.* to bime@'localhost';
drop user bime@'localhost';
flush privileges;