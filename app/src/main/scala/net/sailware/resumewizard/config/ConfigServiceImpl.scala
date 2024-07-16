package net.sailware.resumewizard.config

class ConfigServiceImpl extends ConfigService:

  val dbUsername = sys.env.get("DB_USERNAME")
  val dbPassword = sys.env.get("DB_PASSWORD")
  val dbURL =sys.env.get("DB_URL")
  val databaseConfig = DatabaseConfig(dbURL.get, dbUsername.get, dbPassword.get)

  override def getDatabaseConfig() = databaseConfig
