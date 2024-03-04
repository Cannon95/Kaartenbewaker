    CREATE TABLE IF NOT EXISTS track_records(
        userId varchar(250) NOT NULL PRIMARY KEY NOT NULL,
        username varchar(250),
        region varchar(250) NOT NULL,
        pos int NOT NULL,
        score int NOT NULL
    );