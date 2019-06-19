CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    month_starting_at INTEGER NOT NULL,
    month_billing_day_at INTEGER NOT NULL,
    main_account BOOLEAN NOT NULL,
    show_balance_on_main_account BOOLEAN NOT NULL
);

ALTER TABLE account ADD CONSTRAINT UNQ_ACCOUNT_NAME UNIQUE(NAME);