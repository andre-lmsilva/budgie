--
-- This migration aims to rename the attribute show_balance_on_main_account to show_balance_on_parent_account
-- on the account table.
--
-- @author Andre Silva
UPDATE account SET parent_id = (SELECT id FROM account WHERE main_account = true)
WHERE show_balance_on_main_account = true AND main_account = false;

ALTER TABLE account RENAME COLUMN show_balance_on_main_account TO show_balance_on_parent_account;