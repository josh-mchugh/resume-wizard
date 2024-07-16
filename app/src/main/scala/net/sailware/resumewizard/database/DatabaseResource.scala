package net.sailware.resumewizard.database

import org.jooq.DSLContext

trait DatabaseResource:

  def ctx: DSLContext
