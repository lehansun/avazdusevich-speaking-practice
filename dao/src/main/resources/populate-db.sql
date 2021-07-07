INSERT INTO speaking_practice_app.languages (name) VALUES
('Russian'), ('English');

INSERT INTO speaking_practice_app.countries (name) VALUES
('Belarus');

INSERT INTO `speaking_practice_app`.`customers` (username, firstname, lastname, email, password, native_language_id, learning_language_id, date_of_birth, country_id)
VALUES
('lehansun', 'Aliaksey', 'Vazdusevich', 'lehansun@live.com', 'password', 1, 2, '1988-07-11', 1);

INSERT INTO `speaking_practice_app`.`requests` (initiator_id, language_id, wishedStartTime, wished_end_time)
VALUES (8, 2, '2021-08-11', '2021-08-12');