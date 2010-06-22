set storage_engine = InnoDB;
create database bime;
use bime;
grant ALL PRIVILEGES ON bime.* to bime@'localhost' IDENTIFIED by 'bime';
flush privileges;

