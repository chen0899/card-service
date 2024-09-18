CREATE TABLE card_ranges
(
    bin        VARCHAR(6) PRIMARY KEY,
    min_range  VARCHAR(19),
    max_range  VARCHAR(19),
    alpha_code VARCHAR(2),
    bank_name  VARCHAR(255)
);