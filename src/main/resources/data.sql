INSERT INTO "TEAM" VALUES
                       (201, 1, 'Team1'),
                       (202, 1, 'Team2');



--Insert with order: id, version, role_name
INSERT INTO "ROLE" VALUES
                       (91, 1, 'System-Admin'),
                       (92, 1, 'Support-Member'),
                       (93, 1, 'Support-Coordinator'),
                       (94, 1, 'Management'),
                       (95, 1, 'Customer');

--Insert with order: id, version, email, firstname, lastname,  password, username, role
INSERT INTO "TUSER" VALUES
                        (63, 1, true, 'member0@webstars.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'member0', 92),
                        (64, 1, true, 'member1@webstars.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'member1', 92),
                        (65, 1, true, 'member1@webstars.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'member2', 92),
                        (66, 1, true, 'sysadmin@webstars.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'admin', 91),
                        (67, 1, true, 'manager@webstars.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'manager', 94),
                        (68, 1, true, 'customer@test.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'user', 95),
                        (69, 1, true, 'customer@test2.com', 'Max', 'Mustermann', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', '$2a$10$MwGZcyxNg0LVvWlPjRD10OXWS3axwvaYdB4Bf9v38aWuURQy1yh0O', 'user2', 95);
--                         (96, 1, true, 'Coordinator@webstars.com', 'Max', 'Mustermann', 'password', 'coordinator', 93);

INSERT INTO "WEBSITE" VALUES
                          (61, 1, 'www.test.com', 'www.test.com', 201, 68),
                          (62, 1, 'www.test2.com', 'www.test2.com', 201, 69);

--Insert with order: id, version, assign_date, close_date, closed_by, description_text, header, history, last_update, priority, register_date, registered_by, resolution_text, status, assigned_to, website
INSERT INTO "TICKET" VALUES
                         (70, 1, NULL, NULL, NULL, 'Das ist ein nicht zugewiesenes Ticket', 'Ticket1', '', now(), 1, now(), 'user', '', 'Registered', NULL, 61),
                         (71, 1, now(), now(), NULL, 'Das ist ein geschlossenes Ticket', 'Ticket2', '', now(), 0, now(), 'user2', 'Das ist die Loesung', 'Solved', 63, 62),
                         (72, 1, NULL, NULL, NULL, 'Das ist noch ein nicht zugewiesenes Ticket', 'Ticket3', '', now(), 1, now(), 'user', '', 'Cancelled', NULL, 62),
                         (73, 1, now(), now(), NULL, 'Das ist noch ein geschlossenes Ticket', 'Ticket4', '', now(), 0, now(), 'user2', 'Das ist die Loesung', 'Solved', 63, 62),
                         (74, 1, now(), NULL, NULL, 'Das ist ein zugewiesenes Ticket', 'Ticket5', '', now(), 1, now(), 'user2', '', 'Registered', 63, 62);


INSERT INTO "WEBSITE" VALUES
                          (75, 1, 'www.google.com', 'www.google.com', 201, 68),
                          (76, 1, 'www.ewr.com', 'www.ewr.com', 201, 69),
                          (77, 1, 'www.mci.com', 'www.mci.com', 201, 69),
                          (78, 1, 'www.webstars.com', 'www.webstars.com', 201, NULL),
                          (79, 1, 'www.codepen.com', 'www.codepen.com', 201, NULL),
                          (80, 1, 'www.stackoverflow.com', 'www.stackoverflow.com', 201, NULL),
                          (81, 1, 'www.github.com', 'www.github.com', 201, NULL);


