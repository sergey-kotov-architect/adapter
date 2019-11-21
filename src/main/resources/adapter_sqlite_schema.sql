CREATE TABLE task_result (
  id              INTEGER  PRIMARY KEY,
  start_time      DATETIME NOT NULL,
  end_time        DATETIME NOT NULL,
  submission_time DATETIME NOT NULL,
  task_name       TEXT     NOT NULL,
  task_note       TEXT,
  succeeded       BOOLEAN,
  note            TEXT
);