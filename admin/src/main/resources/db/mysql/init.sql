
INSERT INTO admin_member(active, created_at, updated_at, password, status, username)
values (true, now(), now(), '$2a$10$eekZAv5XHDGdva7Vy5ArMOP/EiCFMZqa6XJuirPGSPjdTOLin6eeq', 'ACTIVE', 'liveclnkadmin');

INSERT INTO role_admin (role_type)
VALUES ('ROLE_ADMIN');

INSERT INTO admin_role (active, created_at, updated_at, admin_member_id, role_admin_id)
VALUES (true, now(), now(), 1, 1);
