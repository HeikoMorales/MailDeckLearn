DROP DATABASE IF EXISTS deck_learn;
CREATE DATABASE deck_learn;
USE deck_learn;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
    user_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    username VARCHAR(25) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    second_name VARCHAR(25) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(50) NOT NULL,
    postal_code VARCHAR(8),
    country VARCHAR(50),
    birth_date DATE,
    register_date DATETIME DEFAULT CURRENT_TIMESTAMP(),
    img_path VARCHAR(200),
    purpose_id MEDIUMINT
);

DROP TABLE IF EXISTS followed;
CREATE TABLE followed (
    followed_id INTEGER NOT NULL,
    follower_id INTEGER NOT NULL,
    follow_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

DROP TABLE IF EXISTS purpose;
CREATE TABLE purpose (
    purpose_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    description VARCHAR(75) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS purpose_relation;
CREATE TABLE purpose_relation (
    user_id INTEGER,
    purpose_id INTEGER
);

DROP TABLE IF EXISTS deck;
CREATE TABLE deck (
    deck_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    creator_id INTEGER,
    title VARCHAR(45),
    description VARCHAR(200),
    img_path VARCHAR(200)
);

DROP TABLE IF EXISTS deck_type;
CREATE TABLE deck_type (
    deck_type_id SMALLINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(75) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS type_relation;
CREATE TABLE type_relation (
    deck_id INTEGER,
    deck_type_id SMALLINT
);

DROP TABLE IF EXISTS card;
CREATE TABLE card (
    card_id INTEGER NOT NULL,
    deck_id INTEGER NOT NULL,
    question VARCHAR(300),
    answer VARCHAR(300),
    img_path VARCHAR(300)
);

DROP TABLE IF EXISTS deck_rate;
CREATE TABLE deck_rate (
    user_id INTEGER,
    deck_id INTEGER,
    rate VARCHAR(2)
);

DROP TABLE IF EXISTS training;
CREATE TABLE training (
    training_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    user_id INTEGER,
    deck_id INTEGER,
    training_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

DROP TABLE IF EXISTS training_session;
CREATE TABLE training_session (
    training_session_id INTEGER,
    training_id INTEGER,
    training_session_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

DROP TABLE IF EXISTS results;
CREATE TABLE results (
    results_id INTEGER,
    training_session_id INTEGER,
    card_id INTEGER,
    box_number VARCHAR(2),
    error_count VARCHAR(3),
    avg_response_time TIME
);

DROP TABLE IF EXISTS card_responses;
CREATE TABLE card_responses(
    card_responses_id INTEGER,
    results_id INTEGER,
    training_session_id INTEGER,
    card_id INTEGER,
    response_time TIME,
    correct BOOLEAN
);

DROP TABLE IF EXISTS saved_deck;
CREATE TABLE saved_deck ( 
    user_id INTEGER,
    deck_id INTEGER,
    saved_date DATETIME DEFAULT CURRENT_TIMESTAMP()
);

ALTER TABLE followed
ADD CONSTRAINT FOLLOWED_FK FOREIGN KEY (followed_id)
    REFERENCES user (user_id),
ADD CONSTRAINT FOLLOWER_FK FOREIGN KEY (follower_id)
    REFERENCES user (user_id),
ADD CONSTRAINT FOLLOWED_PK PRIMARY KEY (followed_id, follower_id);

ALTER TABLE deck
ADD CONSTRAINT DECK_CREATOR_FK FOREIGN KEY (creator_id)
    REFERENCES user (user_id);

ALTER TABLE card
ADD CONSTRAINT DECK_FK FOREIGN KEY (deck_id)
    REFERENCES deck (deck_id),
ADD CONSTRAINT CARD_PK PRIMARY KEY (card_id, deck_id);


ALTER TABLE type_relation 
ADD CONSTRAINT DECK_RELATION_FK FOREIGN KEY (deck_id)
    REFERENCES deck (deck_id),
ADD CONSTRAINT TYPE_RELATION_FK FOREIGN KEY (deck_type_id)
    REFERENCES deck_type (deck_type_id),
ADD CONSTRAINT TYPE_RELATION_PK PRIMARY KEY (deck_id, deck_type_id);

ALTER TABLE purpose_relation
ADD CONSTRAINT USER_ID_PURPOSE_FK FOREIGN KEY (user_id)
    REFERENCES user (user_id),
ADD CONSTRAINT PURPOSE_ID_FK FOREIGN KEY (purpose_id)
    REFERENCES purpose (purpose_id),
ADD CONSTRAINT PURPOSE_RELATION_PK PRIMARY KEY (user_id, purpose_id);

ALTER TABLE saved_deck
ADD CONSTRAINT USER_ID_SAVED_FK FOREIGN KEY (user_id)
    REFERENCES user (user_id),
ADD CONSTRAINT DECK_ID_SAVED_FK FOREIGN KEY (deck_id)
    REFERENCES deck (deck_id),
ADD CONSTRAINT SAVED_DECK_PK PRIMARY KEY (user_id, deck_id);

ALTER TABLE training
ADD CONSTRAINT USER_ID_TRAINING_FK FOREIGN KEY (user_id)
    REFERENCES user (user_id),
ADD CONSTRAINT DECK_ID_TRAINING_FK FOREIGN KEY (deck_id)
    REFERENCES deck (deck_id);

ALTER TABLE training_session
ADD CONSTRAINT TRAINING_ID_FK FOREIGN KEY (training_id)
    REFERENCES training (training_id),
ADD CONSTRAINT TRAINING_SESSION_PK PRIMARY KEY (training_session_id, training_id);

ALTER TABLE results
ADD CONSTRAINT TRAINING_SESSION_ID_FK FOREIGN KEY (training_session_id)
    REFERENCES training_session (training_session_id),
ADD CONSTRAINT CARD_ID_FK FOREIGN KEY (card_id)
    REFERENCES card (card_id),
ADD CONSTRAINT RESULTS_PK PRIMARY KEY (results_id, training_session_id, card_id);

ALTER TABLE card_responses
ADD CONSTRAINT TRAINING_SESSION_ID_CARDRES_FK FOREIGN KEY (training_session_id)
    REFERENCES training_session (training_session_id),
ADD CONSTRAINT CARD_ID_CARDRES_FK FOREIGN KEY (card_id)
    REFERENCES card (card_id),
ADD CONSTRAINT RESULTS_ID_CARDRES_FK FOREIGN KEY (results_id)
    REFERENCES results (results_id),
ADD CONSTRAINT CARDRES_PK PRIMARY KEY (card_responses_id, training_session_id, card_id, results_id);

ALTER TABLE deck_rate
ADD CONSTRAINT USER_ID_FK FOREIGN KEY (user_id)
    REFERENCES user (user_id),
ADD CONSTRAINT DECK_ID_RATE_FK FOREIGN KEY (deck_id)
    REFERENCES deck (deck_id),
ADD CONSTRAINT DECK_RATE_PK PRIMARY KEY (user_id, deck_id);