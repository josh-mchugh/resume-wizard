CREATE TABLE IF NOT EXISTS resume_experiences(
  id SERIAL PRIMARY KEY,
  title varchar(255),
  organization varchar(255),
  duration varchar(255),
  location varchar(255),
  description varchar(2000),
  skills varchar(500)
);
