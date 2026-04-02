CREATE TABLE transactions (
    id VARCHAR(36) NOT NULL,
    account_id VARCHAR(36) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    type VARCHAR(20) NOT NULL,
    date TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);