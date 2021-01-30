CREATE TABLE comment
(
    id int AUTO_INCREMENT PRIMARY KEY,
    content text NOT NULL,
    commentator int NOT NULL,
    parent_id int NOT NULL,
    type int NOT NULL,
    gmt_create bigint NOT NULL,
    gmt_modified bigint NOT NULL,
    like_count bigint DEFAULT 0
);