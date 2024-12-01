CREATE TABLE balance_accounts
(
    id         BIGSERIAL PRIMARY KEY,
    balance    NUMERIC(15, 2) NOT NULL DEFAULT 0,
    owner      VARCHAR(100)   NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at TIMESTAMP      NOT NULL DEFAULT now()
);

CREATE TABLE balance_transactions
(
    id          BIGSERIAL PRIMARY KEY,
    account_id  BIGINT         NOT NULL,
    amount      NUMERIC(15, 2) NOT NULL,
    type        VARCHAR(20)    NOT NULL,
    timestamp   TIMESTAMP DEFAULT now(),
    description TEXT,

    constraint fk_transactions_account foreign key (account_id) REFERENCES balance_accounts (id)
);
