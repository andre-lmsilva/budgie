--
-- This migration script aims to add the IS_TAX_REFUNDABLE field to the record table.
--
-- @author Andre Silva
ALTER TABLE record ADD is_tax_refundable BOOLEAN NOT NULL DEFAULT false;