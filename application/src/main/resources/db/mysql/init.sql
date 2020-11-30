CREATE USER 'livecommerceapi'@'%' IDENTIFIED WITH mysql_native_password BY 'livecommerce@!Local';
CREATE DATABASE livecommerce;

GRANT ALL ON livecommerce.* TO 'livecommerceapi'@'%';


CREATE USER 'livecommercetest'@'%' IDENTIFIED WITH mysql_native_password BY 'livecommerce@!Test';

CREATE DATABASE livecommerce_test;
GRANT ALL ON livecommerce_test.* TO 'livecommercetest'@'%';

FLUSH PRIVILEGES;

INSERT INTO role (role_type) VALUES ('ROLE_GUEST');
INSERT INTO role (role_type) VALUES ('ROLE_USER');


