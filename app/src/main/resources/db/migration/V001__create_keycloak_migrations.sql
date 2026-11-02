CREATE TABLE migrations
(
    version     INT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    timestamp   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);