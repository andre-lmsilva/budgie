--
-- This migration aims to add the parent id attribute to the account table.
--
-- @author Andre Silva
ALTER TABLE account ADD parent_id INTEGER;

ALTER TABLE account ADD CONSTRAINT fk_account_parent_account
FOREIGN KEY(parent_id) REFERENCES account(id)
ON DELETE NO ACTION;