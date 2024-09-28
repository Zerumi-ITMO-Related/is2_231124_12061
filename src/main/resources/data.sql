-- Insert test data into human_being_coordinates table
INSERT INTO human_being_coordinates (x, y) VALUES (10, 20);
INSERT INTO human_being_coordinates (x, y) VALUES (-5, 15);
INSERT INTO human_being_coordinates (x, y) VALUES (30, -40);

-- Insert test data into human_being_car table
INSERT INTO human_being_car (name) VALUES ('Toyota');
INSERT INTO human_being_car (name) VALUES ('Ford');
INSERT INTO human_being_car (name) VALUES (NULL); -- Car with no name

-- Insert test data into human_being table
INSERT INTO human_being (
    name, coordinates_id, creation_date, real_hero, has_toothpick, car_id, mood, impact_speed, minutes_of_waiting, weapon_type
) VALUES
('John Doe', 1, '2023-01-01T12:00:00Z', true, false, 1, 'SADNESS', 120, 15, 'PISTOL'),
('Jane Smith', 2, '2023-02-14T15:30:00Z', false, true, 2, 'SADNESS', 90, 30, 'PISTOL'),
('Alex Johnson', 3, '2023-03-10T09:45:00Z', true, NULL, 3, 'SADNESS', 150, 45, 'PISTOL');
