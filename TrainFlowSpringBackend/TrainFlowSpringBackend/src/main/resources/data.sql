INSERT INTO ROLES(NAME) VALUES ('ROLE_ADMIN');
INSERT INTO ROLES(NAME) VALUES ('ROLE_USER');

INSERT INTO users (name, surname, email, password, age, height, weight, gender)
VALUES ('Ana', 'Anić', 'ana@example.com', '$2a$12$xEYjngqW4iktV0gQXLI3sOFqmv32FdhcDxGYTqEL72fR.siTA6Re6', 28, 165, 60, 'F');

INSERT INTO users (name, surname, email, password, age, height, weight, gender)
VALUES ('Ivan', 'Ivić', 'ivan@example.com', '$2a$12$GSlVH0Nq9uBM461yQdxi6uh45mehl5Z2x7kWB9T/BiutP5tWnPqc.', 32, 180, 82, 'M');


INSERT INTO user_role (user_id, role_id) VALUES (1, 1);

INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
