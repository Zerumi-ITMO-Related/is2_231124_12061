DROP TABLE IF EXISTS human_being;
DROP TABLE IF EXISTS human_being_coordinates;
DROP TABLE IF EXISTS human_being_car;

-- Table for CoordinatesEntity
CREATE TABLE human_being_coordinates (
    id SERIAL PRIMARY KEY,
    x BIGINT NOT NULL,
    y BIGINT NOT NULL
);

-- Table for CarEntity
CREATE TABLE human_being_car (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- Table for HumanBeingEntity
CREATE TABLE human_being (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coordinates_id BIGINT NOT NULL,
    creation_date TIMESTAMPTZ NOT NULL,
    real_hero BOOLEAN NOT NULL,
    has_toothpick BOOLEAN,
    car_id BIGINT NOT NULL,
    mood VARCHAR(50) NOT NULL,
    impact_speed BIGINT NOT NULL,
    minutes_of_waiting INT NOT NULL,
    weapon_type VARCHAR(50) NOT NULL,
    owner_id INT NOT NULL,
    CONSTRAINT fk_coordinates
        FOREIGN KEY (coordinates_id)
        REFERENCES human_being_coordinates (id),
    CONSTRAINT fk_car
        FOREIGN KEY (car_id)
        REFERENCES human_being_car (id),
    CONSTRAINT fk_owner_id
        FOREIGN KEY (owner_id)
        REFERENCES user_lab4 (id)
);
