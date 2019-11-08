--
-- This migration script aims to add the archived attribute to category table in order to implement the logical
-- exclusion of the account.
--
-- @author Andre Silva
ALTER TABLE category ADD archived BOOLEAN NOT NULL DEFAULT false;

CREATE INDEX idx_category_archived ON category(archived);