DELETE FROM record WHERE record_type = 'ImportedRecord';
DELETE FROM imported_file;

ALTER TABLE record DROP CONSTRAINT fk_imported_file_record;
DROP TABLE imported_file;

ALTER TABLE record DROP COLUMN imported_file_id;
ALTER TABLE record DROP COLUMN source_md5hash;