package net.sailware.resumewizard

import java.sql.{Connection, DriverManager}
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import net.sailware.resumewizard.config.ConfigServiceImpl

object Application extends cask.Main:

  val configService = ConfigServiceImpl()
  val databaseConfig = configService.getDatabaseConfig()

  // Run Flyway database migrations
  val flyway = Flyway.configure()
    .dataSource(databaseConfig.url, databaseConfig.username, databaseConfig.password)
    .locations("filesystem:./app/src/main/resources/db/migration")
    .validateMigrationNaming(true)
    .load()
  flyway.migrate()

  // Setup HikariCP for database connection pool
  val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(databaseConfig.url)
  hikariConfig.setUsername(databaseConfig.username)
  hikariConfig.setPassword(databaseConfig.password)

  val dslContext = DSL.using(new HikariDataSource(hikariConfig), SQLDialect.POSTGRES)

  val allRoutes = Seq(
    StaticRoutes(),
    RootRoutes(dslContext)
  )
  











