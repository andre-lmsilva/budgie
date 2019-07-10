ALTER TABLE record ADD COLUMN source_md5hash TEXT;
ALTER TABLE record ADD COLUMN imported_file_id INTEGER;

ALTER TABLE record ADD CONSTRAINT fk_imported_file_record
FOREIGN KEY(imported_file_id) REFERENCES imported_file(id)
ON DELETE CASCADE;
