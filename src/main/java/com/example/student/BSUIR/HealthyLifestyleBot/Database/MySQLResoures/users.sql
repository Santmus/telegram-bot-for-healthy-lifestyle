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

# INSERT INTO project_healthy_lifestyle_users(user_id, user_name, user_surname, user_age, user_height, user_weight, user_list_of_disease) VALUES (25, 'Yauheni', 'Kazachenka', 21, 187, 84.5, 'NULL'),
# (26, 'Vadim', 'Kazachenka', 16, 175, 55, 'Spina');


#1. auto_increment - генерирует уникальный номер при вставке новой записи в таблицу
#2. not_null - поля которое требует обязательное значение, т.е при добавления/обновления 
#необходимо записывать значения в этом поле
#3. null - поля без значения, т.е не является обязательным (NOT NULL; IS NULL) для запросов