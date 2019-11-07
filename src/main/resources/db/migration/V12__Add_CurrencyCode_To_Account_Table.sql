--
-- This migration aims to add the CURRENCY_CODE attribute to ACCOUNT table.
--
-- @author Andre Silva
ALTER TABLE account ADD currency_code TEXT;