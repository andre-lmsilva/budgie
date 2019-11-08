--
-- This migration script aims to add the archived attribute to account table in order to implement the logical
-- exclusion of the account.
--
-- @author Andre Silva
ALTER TABLE account ADD archived BOOLEAN NOT NULL DEFAULT false;

CREATE INDEX idx_account_archived ON account(archived);