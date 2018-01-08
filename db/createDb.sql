create database statistic;

CREATE TABLE file_info (
    id INT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(200) NOT NULL,
    longest_words VARCHAR(4000),
    shortest_words VARCHAR(4000),
    length INT,
    average_word_length DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE line_info (
    file_id INT,
    row_number INT,
    longest_words VARCHAR(4000),
    shortest_words VARCHAR(4000),
    length INT,
    average_word_length DOUBLE,
    FOREIGN KEY (file_id) REFERENCES file_info(id)
);

