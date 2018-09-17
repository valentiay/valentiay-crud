package config

case class Config(
  execution: ExecutionConfig,
  server: ServerConfig,
  service: ServiceConfig,
)

case class ServiceConfig(crud: CrudConfig)
case class CrudConfig()

case class ExecutionConfig(parallelism: Int)
case class ServerConfig(port: String)
