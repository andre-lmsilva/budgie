--
-- This migration script aims to add the target_value attribute to account table in order to allow a ProjectAccount
-- entity use it.
--
-- @author Andre Silva
ALTER TABLE account ADD target_value NUMERIC(62,2);