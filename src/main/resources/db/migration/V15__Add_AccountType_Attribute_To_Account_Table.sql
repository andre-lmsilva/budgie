--
-- This migration script aims to add the account_type attribute to account table in order to be used to identify the
-- property account subtype of the record.
--
-- @author Andre Silva
ALTER TABLE account ADD account_type TEXT NOT NULL DEFAULT 'StandardAccount';

CREATE INDEX idx_account_type ON account(account_type);

CREATE INDEX idx_record_type ON record(record_type);