CREATE TABLE attachment (
    id SERIAL PRIMARY KEY,
    record_id INTEGER NOT NULL,
    attachment_name TEXT NOT NULL,
    mimetype TEXT NOT NULL,
    size_kb NUMERIC(62,2) NOT NULL
);