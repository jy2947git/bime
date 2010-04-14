
INSERT INTO `app_user` (id,created_date,last_update_date,version,account_expired,account_locked,cellPhoneNumber,credentials_expired,email,account_enabled,first_name,homePhoneNumber,last_name,password,password_hint,startDate,superUserId,title,username,workPhoneNumber) VALUES (1,null,null,0,0,0,null,0,'admin@bime.com',1,'admin',null,'lab','d033e22ae348aeb5660fc2140aec35850c4da997','standard',null,null,'administrator of lab','admin',null);
INSERT INTO `user_role` (user_id,role_id) VALUES (1,'ROLE_ADMIN');

INSERT INTO `item_category` (id,created_date,last_update_date,version,name,totalAmount,type,inventoryThreshold) VALUES (1,null,null,1,'te1',3,'te1',10);
INSERT INTO `item_category` (id,created_date,last_update_date,version,name,totalAmount,type,inventoryThreshold) VALUES (2,null,null,1,'te2',4,'te2',10);






