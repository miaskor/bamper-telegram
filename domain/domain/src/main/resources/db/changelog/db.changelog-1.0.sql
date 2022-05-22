-- liquibase formatted sql

-- changeset miaskor:100
CREATE TABLE brand
(
    id                    BIGINT PRIMARY KEY AUTO_INCREMENT,
    brand_name            VARCHAR(50) NOT NULL,
    model                 VARCHAR(50) NOT NULL,
    year_manufacture_from VARCHAR(4)  NOT NULL,
    year_manufacture_to   VARCHAR(4)  NOT NULL
);

-- changeset miaskor:101
CREATE TABLE car
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    body            VARCHAR(20) NOT NULL,
    transmission    VARCHAR(20) NOT NULL,
    engine_capacity FLOAT(4)    NOT NULL,
    fuel_type       VARCHAR(10) NOT NULL,
    engine_type     VARCHAR(15) NOT NULL,
    brand_id        BIGINT      NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

-- changeset miaskor:102
CREATE TABLE car_part
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name_ru VARCHAR(100) NOT NULL,
    name_en VARCHAR(100) NOT NULL
);

-- changeset miaskor:103
CREATE TABLE advertisement
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    article      VARCHAR(20),
    description  VARCHAR(300),
    photo        BLOB,
    price        DOUBLE      NOT NULL,
    sale_percent INT         NOT NULL,
    quality      TINYINT(1)  NOT NULL,
    active       TINYINT(1)  NOT NULL,
    currency     VARCHAR(3)  NOT NULL,
    part_number  VARCHAR(30) NOT NULL,
    car_id       BIGINT      NOT NULL,
    car_part_id  BIGINT      NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car (id),
    FOREIGN KEY (car_part_id) REFERENCES car_part (id)
);

-- changeset miaskor:104
CREATE TABLE store_house
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    store_house_name VARCHAR(30)
);

-- changeset miaskor:105
CREATE TABLE spare_part
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    description    VARCHAR(300),
    photo          BLOB,
    price          DOUBLE      NOT NULL,
    quality        TINYINT(1)  NOT NULL,
    currency       VARCHAR(3)  NOT NULL,
    part_number    VARCHAR(30) NOT NULL,
    car_id         BIGINT      NOT NULL,
    car_part_id    BIGINT      NOT NULL,
    store_house_id BIGINT      NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car (id),
    FOREIGN KEY (car_part_id) REFERENCES car_part (id),
    FOREIGN KEY (store_house_id) REFERENCES store_house (id)
);

-- changeset miaskor:106
CREATE TABLE bamper_client
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    login    VARCHAR(30) NOT NULL,
    password VARCHAR(50) NOT NULL
);

-- changeset miaskor:107
CREATE TABLE telegram_client
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    chat_id          VARCHAR(20) NOT NULL,
    chat_language    VARCHAR(2)  NOT NULL,
    nick_name        VARCHAR(30) NOT NULL,
    bamper_client_id BIGINT      NOT NULL,
    FOREIGN KEY (bamper_client_id) REFERENCES bamper_client (id)
);

-- changeset miaskor:108
CREATE TABLE worker_telegram
(
    worker_telegram_client_id   BIGINT NOT NULL,
    employer_telegram_client_id BIGINT NOT NULL,
    FOREIGN KEY (worker_telegram_client_id) REFERENCES telegram_client (id),
    FOREIGN KEY (employer_telegram_client_id) REFERENCES telegram_client (id)
);

-- changeset miaskor:109
CREATE TABLE worker_store_house
(
    worker_telegram_client_id BIGINT     NOT NULL,
    store_house_id            BIGINT     NOT NULL,
    worker_privilege          VARCHAR(4) NOT NULL,
    FOREIGN KEY (worker_telegram_client_id) REFERENCES telegram_client (id),
    FOREIGN KEY (store_house_id) REFERENCES store_house (id)
);

-- changeset miaskor:110
CREATE TABLE telegram_client_store_house
(
    telegram_client_id BIGINT NOT NULL,
    store_house_id     BIGINT NOT NULL,
    FOREIGN KEY (telegram_client_id) REFERENCES telegram_client (id),
    FOREIGN KEY (store_house_id) REFERENCES store_house (id)
);

