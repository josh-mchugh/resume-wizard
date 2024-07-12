CREATE TABLE IF NOT EXISTS resume_certifications(
  id SERIAL PRIMARY KEY,
  title varchar(255),
  organization varchar(255),
  year varchar(255),
  location varchar(255)
);
