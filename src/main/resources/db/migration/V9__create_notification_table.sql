create table notification
(
  id int AUTO_INCREMENT PRIMARY KEY,
  notifier int NOT NULL,
  receiver int NOT NULL,
  type int NOT NULL,
  status int NOT NULL,
  outer_id int not null,
  outer_title varchar(256) not null,
  gmt_create bigint NOT NULL
)