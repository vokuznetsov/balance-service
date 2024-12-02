CREATE TABLE balance_accounts
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR        NOT NULL,
    balance    NUMERIC(15, 2) NOT NULL DEFAULT 0,
    deleted    BOOLEAN        NOT NULL DEFAULT false,
    created_at TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at TIMESTAMP      NOT NULL DEFAULT now()
);

CREATE TABLE balance_transactions
(
    id          BIGSERIAL PRIMARY KEY,
    account_id  BIGINT         NOT NULL,
    amount      NUMERIC(15, 2) NOT NULL,
    type        INT            NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT now(),

    constraint fk_transactions_account foreign key (account_id) REFERENCES balance_accounts (id)
);
