CREATE TABLE task(
    task_id                 TEXT        NOT NULL PRIMARY KEY,
    assigned_to_employee_id TEXT        NULL,
    status                  TEXT        NOT NULL,
    assigned_task_value     NUMERIC     NOT NULL,
    completed_task_value    NUMERIC     NOT NULL
);

CREATE TABLE employee(
    employee_id  TEXT        NOT NULL PRIMARY KEY,
    first_name   TEXT        NOT NULL,
    last_name    TEXT        NOT NULL,
    phone_number TEXT        NOT NULL,
    email        TEXT        NOT NULL,
    slack        TEXT        NOT NULL,
    role         TEXT        NOT NULL,

    CONSTRAINT uq_phone_number UNIQUE(phone_number)
);

CREATE TABLE transaction(
    transaction_id TEXT        NOT NULL PRIMARY KEY DEFAULT substr(md5(random()::text), 0, 20),
    amount         NUMERIC     NOT NULL,
    type           TEXT        NOT NULL,
    to_account     TEXT        NOT NULL,
    from_account   TEXT        NOT NULL,
    task_id        TEXT        NULL,
    created_at     TIMESTAMPTZ NOT NULL
);

CREATE TABLE account(
    account_id      TEXT        NOT NULL PRIMARY KEY DEFAULT substr(md5(random()::text), 0, 20),
    amount          NUMERIC     NOT NULL,
    status          TEXT        NOT NULL,
    last_closed_day DATE,
    last_update_at  TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    version         NUMERIC     NOT NULL
);
