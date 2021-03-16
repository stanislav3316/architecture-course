CREATE TABLE employee(
    employee_id  TEXT        NOT NULL PRIMARY KEY DEFAULT substr(md5(random()::text), 0, 20),
    first_name   TEXT        NOT NULL,
    last_name    TEXT        NOT NULL,
    phone_number TEXT        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,
    version      NUMERIC     NOT NULL,

    CONSTRAINT uq_phone_number UNIQUE(phone_number)
);