CREATE TABLE employee(
    employee_id  TEXT        NOT NULL PRIMARY KEY,
    first_name   TEXT        NOT NULL,
    last_name    TEXT        NOT NULL,
    phone_number TEXT        NOT NULL,
    email        TEXT        NOT NULL,
    slack        TEXT        NOT NULL,
    role         TEXT        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL
);

CREATE TABLE task(
    task_id                 TEXT        NOT NULL PRIMARY KEY,
    title                   TEXT        NOT NULL,
    description             TEXT        NOT NULL,
    assigned_to_employee_id TEXT        NULL,
    created_by_employee_id  TEXT        NOT NULL,
    assigned_task_value     NUMERIC     NULL,
    completed_task_value    NUMERIC     NULL,
    status                  TEXT        NOT NULL,
    created_at              TIMESTAMPTZ NOT NULL
);

CREATE TABLE account(
    account_id      TEXT        NOT NULL PRIMARY KEY,
    employee_id     TEXT        NOT NULL,
    last_closed_day DATE        NULL,
    created_at      TIMESTAMPTZ NOT NULL
);

CREATE TABLE transaction(
    transaction_id TEXT        NOT NULL PRIMARY KEY,
    amount         NUMERIC     NOT NULL,
    type           TEXT        NOT NULL,
    to_account     TEXT        NOT NULL,
    from_account   TEXT        NOT NULL,
    task_id        TEXT        NULL,
    created_at     TIMESTAMPTZ NOT NULL
);

CREATE TABLE raw_event(
    event_id      TEXT        NOT NULL PRIMARY KEY,
    event_name    TEXT        NOT NULL,
    event_version INT         NOT NULL,
    producer      TEXT        NOT NULL,
    event_time    TIMESTAMPTZ NOT NULL,
    raw           JSONB       NOT NULL
);
