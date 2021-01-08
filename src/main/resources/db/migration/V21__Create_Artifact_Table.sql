CREATE TABLE artifact (
    id INTEGER PRIMARY KEY,
    content BYTEA NOT NULL
);

ALTER TABLE artifact
ADD CONSTRAINT fk_attachment_artifact
FOREIGN KEY (id)
REFERENCES attachment(id)
ON DELETE CASCADE;