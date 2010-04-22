create table Refrigerator (id bigint not null, primary key (id));
create table app_user (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, account_expired bit not null, account_locked bit not null, birthDate datetime, cellPhoneNumber varchar(255), credentials_expired bit not null, email varchar(255), account_enabled bit, first_name varchar(50) not null, homePhoneNumber varchar(255), last_name varchar(50) not null, password varchar(255) not null, password_hint varchar(255), startDate datetime, superUserId bigint, title varchar(255), username varchar(50) not null unique, workPhoneNumber varchar(255), primary key (id));
create table chemical_shelves (id bigint not null, primary key (id));
create table equiptment (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, contactUserName varchar(50), equiptmentCondition varchar(50), lastUserName varchar(50), location varchar(50), name varchar(50) not null, type varchar(50) not null, primary key (id));
create table experiment_image (stored_file_id bigint not null, experiment_note_id bigint, primary key (stored_file_id));
create table experiment_note (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, endDate datetime, name varchar(255), notes longtext, startDate datetime, experiment_protocol_id bigint, managed_project_id bigint, user_id bigint, primary key (id));
create table experiment_protocol (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, createdByName varchar(50), experimentProcedure longtext, name varchar(50) not null, protocolVersion integer not null, managed_project_id bigint, primary key (id));
create table experiment_protocol_audit (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, message longtext not null, experiment_protocol_id bigint, primary key (id));
create table experimental_animal (id bigint not null, primary key (id));
create table experimental_material (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, name varchar(50) not null, primary key (id));
create table experimental_mice (id bigint not null, primary key (id));
create table inventory_audit (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, message longtext, managed_item_id bigint, primary key (id));
create table item_category (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, inventoryThreshold integer, name varchar(50) not null, totalAmount integer not null, type varchar(50) not null, primary key (id), unique (name, type));
create table lab (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, name varchar(255) not null, password varchar(255), storageIdentity varchar(255), primary key (id));
create table lab_meetin_item (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, itemOrder integer, topic varchar(255), lab_meeting_id bigint, speaker_id bigint, primary key (id));
create table lab_meeting (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, endCalendar datetime, message longtext, startCalendar datetime, subject varchar(50), coordinator_id bigint, primary key (id));
create table managed_grant (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, name varchar(255) not null, primary key (id));
create table managed_item (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, amount integer not null, expirationDate datetime, lastUserName varchar(50), maker varchar(50), storageNotes varchar(50), storePersonId varchar(50), storigibleUniqueId varchar(50), item_category_id bigint, order_item_id bigint, primary key (id));
create table managed_order (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, accountNumber varchar(50), approvalDate datetime, approvedByPerson bigint, fundName varchar(255), orderByPerson bigint, orderDate datetime, salesEmail varchar(255), salesFirstName varchar(255), salesLastName varchar(255), salesPhone varchar(255), status varchar(255), submitDate datetime, submittedByPerson bigint, totalPrice decimal(19,2), primary key (id));
create table managed_project (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, description varchar(255), endDate datetime, fundSource varchar(255), name varchar(255) not null, startDate datetime, status varchar(255), primary key (id));
create table meeting_file (stored_file_id bigint not null, lab_meeting_id bigint, primary key (stored_file_id));
create table meeting_participants (meeting_id bigint not null, user_id bigint not null, primary key (meeting_id, user_id));
create table note_access (note_id bigint not null, user_id bigint not null, primary key (note_id, user_id));
create table order_item (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, amount integer not null, maker varchar(50), supplier varchar(255), totalCost decimal(19,2) not null, unitPrice decimal(19,2), item_category_id bigint, order_id bigint, primary key (id));
create table project_owner (project_id bigint not null, user_id bigint not null, primary key (project_id, user_id));
create table project_participants (project_id bigint not null, user_id bigint not null, primary key (project_id, user_id));
create table resource_user_authorization (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, permission varchar(255), resourceId bigint, resourceType varchar(255), userName varchar(255), primary key (id));
create table role (id varchar(255) not null, description varchar(255), version integer, primary key (id));
create table storage (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, alias varchar(100) not null, location varchar(50), name varchar(50) not null, type varchar(50) not null, contactPerson_id bigint, primary key (id));
create table storage_others (id bigint not null, primary key (id));
create table storage_section (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, alias varchar(100) not null, name varchar(50), type varchar(50), storage_id bigint, primary key (id));
create table stored_file (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, backupStorageIdentifier varchar(255), encrypted bit not null, fileName varchar(100) not null, fileType varchar(255), fullPath varchar(255), fullUrl varchar(255), uniqueStorageIdentifier varchar(255), uploaded_by_user_id bigint, primary key (id));
create table to_do (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, endDate datetime, message varchar(255), startDate datetime, status varchar(255), subject varchar(255), project_id bigint, primary key (id));
create table user_role (user_id bigint not null, role_id varchar(255) not null, primary key (user_id, role_id));
create table work_log (id bigint not null auto_increment, created_date datetime, last_update_date datetime, updatedByUser varchar(255), version integer, message varchar(200), to_do_id bigint, primary key (id));
alter table Refrigerator add index FK27A905802CD4F4DF (id), add constraint FK27A905802CD4F4DF foreign key (id) references storage (id);
alter table chemical_shelves add index FK470D593B2CD4F4DF (id), add constraint FK470D593B2CD4F4DF foreign key (id) references storage (id);
alter table experiment_image add index FK4C793699F5D09FF8 (stored_file_id), add constraint FK4C793699F5D09FF8 foreign key (stored_file_id) references stored_file (id);
alter table experiment_image add index FK4C7936996E0DDA6C (experiment_note_id), add constraint FK4C7936996E0DDA6C foreign key (experiment_note_id) references experiment_note (id);
alter table experiment_note add index FKD92F82F488EC47AC (experiment_protocol_id), add constraint FKD92F82F488EC47AC foreign key (experiment_protocol_id) references experiment_protocol (id);
alter table experiment_note add index FKD92F82F4D4336131 (user_id), add constraint FKD92F82F4D4336131 foreign key (user_id) references app_user (id);
alter table experiment_note add index FKD92F82F4C6E999B2 (managed_project_id), add constraint FKD92F82F4C6E999B2 foreign key (managed_project_id) references managed_project (id);
alter table experiment_protocol add index FK4601109AC6E999B2 (managed_project_id), add constraint FK4601109AC6E999B2 foreign key (managed_project_id) references managed_project (id);
alter table experiment_protocol_audit add index FKDF1EB3F688EC47AC (experiment_protocol_id), add constraint FKDF1EB3F688EC47AC foreign key (experiment_protocol_id) references experiment_protocol (id);
alter table experimental_animal add index FKC9B3D933618744A1 (id), add constraint FKC9B3D933618744A1 foreign key (id) references experimental_material (id);
alter table experimental_mice add index FKB0D8CFF5618744A1 (id), add constraint FKB0D8CFF5618744A1 foreign key (id) references experimental_material (id);
alter table inventory_audit add index FKC1C0A3382595142 (managed_item_id), add constraint FKC1C0A3382595142 foreign key (managed_item_id) references managed_item (id);
alter table lab_meetin_item add index FK71A0881493EA2596 (lab_meeting_id), add constraint FK71A0881493EA2596 foreign key (lab_meeting_id) references lab_meeting (id);
alter table lab_meetin_item add index FK71A088143B55107D (speaker_id), add constraint FK71A088143B55107D foreign key (speaker_id) references app_user (id);
alter table lab_meeting add index FKE737B589FB6E9BAC (coordinator_id), add constraint FKE737B589FB6E9BAC foreign key (coordinator_id) references app_user (id);
alter table managed_item add index FK31470FB3429B5060 (order_item_id), add constraint FK31470FB3429B5060 foreign key (order_item_id) references order_item (id);
alter table managed_item add index FK31470FB34B239A58 (item_category_id), add constraint FK31470FB34B239A58 foreign key (item_category_id) references item_category (id);
alter table meeting_file add index FKA0509100F5D09FF8 (stored_file_id), add constraint FKA0509100F5D09FF8 foreign key (stored_file_id) references stored_file (id);
alter table meeting_file add index FKA050910093EA2596 (lab_meeting_id), add constraint FKA050910093EA2596 foreign key (lab_meeting_id) references lab_meeting (id);
alter table meeting_participants add index FKEC9ECF24D4336131 (user_id), add constraint FKEC9ECF24D4336131 foreign key (user_id) references app_user (id);
alter table meeting_participants add index FKEC9ECF24B7D3FBA4 (meeting_id), add constraint FKEC9ECF24B7D3FBA4 foreign key (meeting_id) references lab_meeting (id);
alter table note_access add index FK5D8D8E51CCFDA4AE (note_id), add constraint FK5D8D8E51CCFDA4AE foreign key (note_id) references experiment_note (id);
alter table note_access add index FK5D8D8E51D4336131 (user_id), add constraint FK5D8D8E51D4336131 foreign key (user_id) references app_user (id);
alter table order_item add index FK2D110D644B239A58 (item_category_id), add constraint FK2D110D644B239A58 foreign key (item_category_id) references item_category (id);
alter table order_item add index FK2D110D647F87B2F2 (order_id), add constraint FK2D110D647F87B2F2 foreign key (order_id) references managed_order (id);
alter table project_owner add index FKC7D82A0D4F6DFD12 (project_id), add constraint FKC7D82A0D4F6DFD12 foreign key (project_id) references managed_project (id);
alter table project_owner add index FKC7D82A0DD4336131 (user_id), add constraint FKC7D82A0DD4336131 foreign key (user_id) references app_user (id);
alter table project_participants add index FKB1C9A7064F6DFD12 (project_id), add constraint FKB1C9A7064F6DFD12 foreign key (project_id) references managed_project (id);
alter table project_participants add index FKB1C9A706D4336131 (user_id), add constraint FKB1C9A706D4336131 foreign key (user_id) references app_user (id);
alter table storage add index FK8FB0427B92128D47 (contactPerson_id), add constraint FK8FB0427B92128D47 foreign key (contactPerson_id) references app_user (id);
alter table storage_others add index FKC67F6DA72CD4F4DF (id), add constraint FKC67F6DA72CD4F4DF foreign key (id) references storage (id);
alter table storage_section add index FKC32EBD215D5EC123 (storage_id), add constraint FKC32EBD215D5EC123 foreign key (storage_id) references storage (id);
alter table stored_file add index FKAEDA5898708DC888 (uploaded_by_user_id), add constraint FKAEDA5898708DC888 foreign key (uploaded_by_user_id) references app_user (id);
alter table to_do add index FK6968CCF4F6DFD12 (project_id), add constraint FK6968CCF4F6DFD12 foreign key (project_id) references managed_project (id);
alter table user_role add index FK143BF46A2F089D51 (role_id), add constraint FK143BF46A2F089D51 foreign key (role_id) references role (id);
alter table user_role add index FK143BF46AD4336131 (user_id), add constraint FK143BF46AD4336131 foreign key (user_id) references app_user (id);
alter table work_log add index FK218A91686A56A48 (to_do_id), add constraint FK218A91686A56A48 foreign key (to_do_id) references to_do (id);
