CREATE TABLE IF NOT EXISTS resume_details (
  id SERIAL PRIMARY KEY,
  name varchar(255),
  title varchar(255),
  summary varchar(510),
  phone varchar(255),
  email varchar(255),
  location varchar(255)
);
