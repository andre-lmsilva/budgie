 ALTER TABLE record ADD COLUMN record_container_id INTEGER;

ALTER TABLE record ADD CONSTRAINT fk_record_container_record
FOREIGN KEY(record_container_id) REFERENCES record_container(id)
ON DELETE CASCADE;