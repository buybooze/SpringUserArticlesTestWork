DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users ;
DROP TABLE IF EXISTS tokens ;


CREATE TABLE IF NOT EXISTS articles(
    id SERIAL,
    author VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content CLOB,
    creationTimestamp BIGINT NOT NULL,
    identicon BLOB
);


CREATE TABLE IF NOT EXISTS users (
    id SERIAL,
    name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    roles VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS tokens (
    id SERIAL,
    token VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    expiry_date DATE NOT NULL,
);