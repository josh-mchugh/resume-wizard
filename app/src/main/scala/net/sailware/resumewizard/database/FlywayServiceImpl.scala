package net.sailware.resumewizard.database

import net.sailware.resumewizard.config.ConfigService
import org.flywaydb.core.Flyway

class FlywayServiceImpl(configService: ConfigService) extends FlywayService:

  val flyway = Flyway.configure()
    .dataSource(
      configService.getDatabaseConfig().url,
      configService.getDatabaseConfig().username,
      configService.getDatabaseConfig().password
    )
    .locations("filesystem:./app/src/main/resources/db/migration")
    .validateMigrationNaming(true)
    .load()

  override def migrate(): Unit = flyway.migrate()
