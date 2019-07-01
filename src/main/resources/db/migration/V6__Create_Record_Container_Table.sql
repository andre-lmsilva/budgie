CREATE TABLE IF NOT EXISTS record_container (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    record_container_type TEXT NOT NULL
);

ALTER TABLE record_container ADD CONSTRAINT unq_record_container_name UNIQUE(name);