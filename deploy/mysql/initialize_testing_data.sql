
INSERT INTO `app_user` (id,created_date,last_update_date,version,account_expired,account_locked,cellPhoneNumber,credentials_expired,email,account_enabled,first_name,homePhoneNumber,last_name,password,password_hint,startDate,superUserId,title,username,workPhoneNumber) VALUES (2,null,null,0,0,0,null,0,'junqiang_you@yahoo.com',1,'User',null,'Super','a94a8fe5ccb19ba61c4c0873d391e987982fbbd3','test',null,null,null,'super','');
INSERT INTO `app_user` (id,created_date,last_update_date,version,account_expired,account_locked,cellPhoneNumber,credentials_expired,email,account_enabled,first_name,homePhoneNumber,last_name,password,password_hint,startDate,superUserId,title,username,workPhoneNumber) VALUES (3,null,null,0,0,0,null,0,'junqiang_you@yahoo.com',1,'User',null,'Regular','a94a8fe5ccb19ba61c4c0873d391e987982fbbd3','test',null,2,null,'user','');
INSERT INTO `user_role` (user_id,role_id) VALUES (2,'ROLE_SUPER_USER');
INSERT INTO `user_role` (user_id,role_id) VALUES (2,'ROLE_USER');
INSERT INTO `user_role` (user_id,role_id) VALUES (3,'ROLE_USER');

INSERT INTO `storage` (id,created_date,last_update_date,version,contactPersion,location,name,type) VALUES (1,null,null,0,'test','test','r13','4');
INSERT INTO `storage` (id,created_date,last_update_date,version,contactPersion,location,name,type) VALUES (2,null,null,0,'test','test','o1','test');
INSERT INTO `storage` (id,created_date,last_update_date,version,contactPersion,location,name,type) VALUES (3,{ts '2009-10-20 23:57:55.000'},{ts '2009-10-20 23:57:55.000'},0,'me','corner','MyRefri','4');


INSERT INTO `storage_others` (id) VALUES (2);
INSERT INTO `Refrigerator` (id) VALUES (3);

INSERT INTO `storage_section` (id,created_date,last_update_date,version,name,type,storage_id) VALUES (1,null,null,0,'s1','test',1);
INSERT INTO `storage_section` (id,created_date,last_update_date,version,name,type,storage_id) VALUES (2,null,null,0,'s2','test',1);
INSERT INTO `storage_section` (id,created_date,last_update_date,version,name,type,storage_id) VALUES (3,null,null,0,'s1','test',2);
INSERT INTO `storage_section` (id,created_date,last_update_date,version,name,type,storage_id) VALUES (4,null,null,0,'s2','test',2);
INSERT INTO `storage_section` (id,created_date,last_update_date,version,name,type,storage_id) VALUES (5,{ts '2009-10-20 23:58:05.000'},{ts '2009-10-20 23:58:05.000'},0,'test1','test1',3);
INSERT INTO `storage_section` (id,created_date,last_update_date,version,name,type,storage_id) VALUES (6,{ts '2009-10-20 23:58:09.000'},{ts '2009-10-20 23:58:09.000'},0,'test2','test2',3);
