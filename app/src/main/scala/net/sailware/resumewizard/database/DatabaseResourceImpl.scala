package net.sailware.resumewizard.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.{Connection, DriverManager}
import net.sailware.resumewizard.config.ConfigService
import org.jooq.SQLDialect
import org.jooq.impl.DSL

class DatabaseResourceImpl(configService: ConfigService) extends DatabaseResource:

  // Setup HikariCP for database connection pool
  val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(configService.getDatabaseConfig().url)
  hikariConfig.setUsername(configService.getDatabaseConfig().username)
  hikariConfig.setPassword(configService.getDatabaseConfig().password)

  val dslContext = DSL.using(new HikariDataSource(hikariConfig), SQLDialect.SQLITE)

  override def ctx = dslContext
