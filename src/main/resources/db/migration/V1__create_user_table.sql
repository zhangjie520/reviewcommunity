create table user
(
  id int auto_increment primary key,
  account_id varchar (100),
  name varchar (50),
  token varchar(36),
  bio varchar(100),
  gmt_create bigint,
  gmt_modified bigint
);