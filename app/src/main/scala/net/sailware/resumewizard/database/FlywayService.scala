package net.sailware.resumewizard.database

trait FlywayService:

  def migrate(): Unit
