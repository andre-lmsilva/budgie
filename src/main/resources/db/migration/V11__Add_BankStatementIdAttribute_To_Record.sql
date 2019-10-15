--
-- This script aims to add an extra text field to the record table to keep track of the respective record as it appears
-- on the bank statement.
--
-- @author Andre Silva
ALTER TABLE record ADD COLUMN bank_statement_id VARCHAR(140);