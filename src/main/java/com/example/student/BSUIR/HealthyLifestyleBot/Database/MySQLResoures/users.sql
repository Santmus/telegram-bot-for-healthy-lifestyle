USE telegrambot;

DROP TABLE IF EXISTS project_healthy_lifestyle_users;

CREATE TABLE project_healthy_lifestyle_users(
user_id INT NOT NULL PRIMARY KEY, 
user_name VARCHAR(20) NULL, 				
user_surname VARCHAR(20) NULL,
user_age INT NULL,
user_height FLOAT NULL,
user_weight FLOAT NULL, 
user_list_of_disease VARCHAR(1000) NULL
);
