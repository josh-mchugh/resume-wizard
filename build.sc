import mill._, scalalib._
import mill.util.Jvm

object app extends ScalaModule {
  def scalaVersion = "3.3.3"

  def resolutionCustomizer = T.task {
    Some((r: coursier.core.Resolution) =>
      r.withOsInfo(coursier.core.Activation.Os.fromProperties(sys.props.toMap))
    )
  }

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.2",
    ivy"com.lihaoyi::scalatags:0.13.1",
    ivy"org.jooq:jooq:3.19.10",
    ivy"org.xerial:sqlite-jdbc:3.46.0.1",
    ivy"org.flywaydb:flyway-core:10.15.2",
    ivy"com.zaxxer:HikariCP:5.1.0",
    ivy"org.openjfx:javafx-base:22.0.2",
    ivy"org.openjfx:javafx-web:22.0.2",
    ivy"org.scalafx::scalafx:22.0.0-R33"
  )

  object test extends ScalaTests {
    def testFramework = "utest.runner.Framework"
    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest::0.8.3",
      ivy"com.lihaoyi::requests::0.8.3",
      ivy"io.undertow:undertow-core:2.3.10.Final",
    )
  }

  override def generatedSources = T {
    super.generatedSources() ++ os.walk(jooq()).filter(path => os.isFile(path) && Set("scala", "java").contains(path.ext)).map(PathRef(_))
  }

  def jooqClassPath: T[Agg[PathRef]] = T {
    resolveDeps(T.task {
      Agg(
        ivy"org.jooq:jooq:3.19.10",
        ivy"org.jooq:jooq-codegen:3.19.10",
        ivy"org.jooq:jooq-meta:3.19.10",
        ivy"org.jooq:jooq-meta-extensions:3.19.10",
      ).map(bindDependency())
    })()
 }

  def jooq = T {
    val xmlConfig =
      s"""
        <configuration>
  	  <generator>
	    <database>
	      <name>org.jooq.meta.extensions.ddl.DDLDatabase</name>
              <properties>
                <!-- Specify the location of your SQL script.
                     You may use ant-style file matching, e.g. /path/**/to/*.sql

                     Where:
                     - ** matches any directory subtree
                     - * matches any number of characters in a directory / file name
                     - ? matches a single character in a directory / file name 
                -->
                <property>
                  <key>scripts</key>
                  <value>${millSourcePath}/src/main/resources/db/migration/*.sql</value>
                </property>
                <!-- The sort order of the scripts within a directory, where:

                  - semantic: sorts versions, e.g. v-3.10.0 is after v-3.9.0 (default)
                  - alphanumeric: sorts strings, e.g. v-3.10.0 is before v-3.9.0
                  - flyway: sorts files the same way as flyway does
                  - none: doesn't sort directory contents after fetching them from the directory 
                -->
                <property>
                  <key>sort</key>
                  <value>flyway</value>
                </property>
                <!-- The default schema for unqualified objects:

                  - public: all unqualified objects are located in the PUBLIC (upper case) schema
                  - none: all unqualified objects are located in the default schema (default)

                  This configuration can be overridden with the schema mapping feature 
                -->
                <property>
                  <key>unqualifiedSchema</key>
                  <value>none</value>
                </property>
                <!-- The default name case for unquoted objects:

                  - as_is: unquoted object names are kept unquoted
                  - upper: unquoted object names are turned into upper case (most databases)
                  - lower: unquoted object names are turned into lower case (e.g. PostgreSQL) -->
                <property>
                  <key>defaultNameCase</key>
                  <value>upper</value>
                </property>
              </properties>
            </database>
            <target>
              <packageName>net.sailware.resumewizard.jooq</packageName>
	      <directory>${T.dest}</directory>
            </target>
          </generator>
        </configuration>
      """

    os.write(T.dest / "config.xml", xmlConfig)

    Jvm.runSubprocess(
      mainClass = "org.jooq.codegen.GenerationTool",
      classPath = jooqClassPath().map(_.path),
      jvmArgs = forkArgs(),
      envArgs = Map(),
      mainArgs = Seq("config.xml"),
      workingDir = T.dest
    )

    T.dest
  }
}