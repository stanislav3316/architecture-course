CREATE TABLE employee(
    employee_id  TEXT        NOT NULL PRIMARY KEY DEFAULT substr(md5(random()::text), 0, 20),
    first_name   TEXT        NOT NULL,
    last_name    TEXT        NOT NULL,
    phone_number TEXT        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,
    version      NUMERIC     NOT NULL,

    CONSTRAINT uq_phone_number UNIQUE(phone_number)
);

CREATE TABLE task(
    task_id                 TEXT        NOT NULL PRIMARY KEY DEFAULT substr(md5(random()::text), 0, 10),
    title                   TEXT        NOT NULL,
    description             TEXT        NOT NULL,
    assigned_to_employee_id TEXT        NULL,
    created_by_employee_id  TEXT        NOT NULL,
    status                  TEXT        NOT NULL,
    created_at              TIMESTAMPTZ NOT NULL,
    version                 NUMERIC     NOT NULL
);
