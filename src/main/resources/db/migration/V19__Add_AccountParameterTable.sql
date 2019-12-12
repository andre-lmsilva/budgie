--
-- This migration script aims to create the ACCOUNT_PARAMETER table which purpose is to hold pair of key and value
-- data related with the account.
--
-- @author Andre Silva
CREATE TABLE account_parameter (
    id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL,
    parameter_key TEXT NOT NULL,
    parameter_value TEXT NOT NULL
);

ALTER TABLE account_parameter
ADD CONSTRAINT fk_account_account_parameter
FOREIGN KEY (account_id) REFERENCES account(id)
ON DELETE CASCADE;