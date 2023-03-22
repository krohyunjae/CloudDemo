DELIMITER //
	CREATE PROCEDURE populate_test_user(IN first_name varchar(25), last_name varchar(25), username varchar(25))
		BEGIN
			IF(SELECT count(*) FROM app_user WHERE email = username) = 0 THEN
				INSERT INTO app_user(email, first_name, last_name, `password`) VALUES (username, first_name, last_name, concat(first_name, '123'));
			END IF;
		END //
DELIMITER ;

CALL populate_test_user('John', 'Smith', 'johnsmith@gmail.com');
CALL populate_test_user('John', 'Smith', 'johnsmith@gmail.com');
DROP PROCEDURE populate_test_user;