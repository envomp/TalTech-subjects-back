CREATE TABLE IF NOT EXISTS subject (
  id BIGSERIAL NOT NULL,
  code VARCHAR(20) UNIQUE,
  course_name VARCHAR(255),
  course_name_eng VARCHAR(255),
  eap INTEGER,
  semesters VARCHAR(100),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL NOT NULL,
  password_hash VARCHAR(255),
  role INTEGER,
  salt VARCHAR(255),
  username VARCHAR(255) UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS teachers (
  id BIGSERIAL NOT NULL,
  name VARCHAR(255) UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS subject_comment (
  id BIGSERIAL NOT NULL,
  comment VARCHAR(2000),
  time TIMESTAMP,
  subject_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (subject_id) REFERENCES subject (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS subject_review (
  id BIGSERIAL NOT NULL,
  rating INTEGER,
  review VARCHAR(2000),
  time TIMESTAMP,
  subject_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (id),
  FOREIGN KEY (subject_id) REFERENCES subject (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT unique_user_subject UNIQUE(user_id, subject_id)
);

CREATE TABLE IF NOT EXISTS user_subject (
  subject_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (subject_id, user_id),
  FOREIGN KEY (subject_id) REFERENCES subject (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teacher_subject (
  subject_id BIGINT,
  teacher_id BIGINT,
  PRIMARY KEY (subject_id, teacher_id),
  FOREIGN KEY (subject_id) REFERENCES subject (id) ON DELETE CASCADE,
  FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON DELETE CASCADE
);

-- CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence INCREMENT 1 START 1;