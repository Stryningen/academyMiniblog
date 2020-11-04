CREATE SCHEMA miniblog;

CREATE TABLE miniblog.user
(
    id        INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    email     VARCHAR(50) UNIQUE
);

CREATE TABLE miniblog.article
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    article_name VARCHAR(50) UNIQUE,
    article_body VARCHAR(2000),
    created_date DATETIME
);

CREATE TABLE miniblog.author_article
(
    id         INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id    INTEGER,
    article_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES article (id) ON DELETE CASCADE,
    UNIQUE unique_index(user_id, article_id)
);

CREATE TABLE miniblog.comment
(
    id           INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id      INTEGER,
    article_id   INTEGER,
    comment      VARCHAR(500),
    created_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES article (id) ON DELETE CASCADE
);
