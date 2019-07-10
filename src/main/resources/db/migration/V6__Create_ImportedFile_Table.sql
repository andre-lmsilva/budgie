CREATE TABLE IF NOT EXISTS imported_file(
    id SERIAL PRIMARY KEY,
    imported_at TIMESTAMP NOT NULL,
    file_name TEXT NOT NULL,
    file_mime_type TEXT NOT NULL,
    md5file_hash TEXT NOT NULL,
    file_size BIGINT NOT NULL,
    encoded_content TEXT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

ALTER TABLE imported_file
ADD CONSTRAINT unq_imported_file_md5_file_hash
UNIQUE(md5file_hash);
