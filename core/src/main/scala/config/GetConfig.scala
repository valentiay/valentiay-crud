package config

import com.typesafe.config.Config

import language.experimental.macros
import magnolia._

trait GetConfig[T] {
  def get(config: Config, name: String): T
}

object GetConfig {
  implicit val getIntConfig: GetConfig[Int] = (config: Config, name: String) => config.getInt(name)
  implicit val getStringConfig: GetConfig[String] = (config: Config, name: String) => config.getString(name)

  type Typeclass[T] = GetConfig[T]

  def combine[T](caseClass: CaseClass[GetConfig, T]): GetConfig[T] = (config: Config, name: String) => {
    caseClass.construct(p => p.typeclass.get(config.getConfig(name), p.label))
  }

  def dispatch[T](sealedTrait: SealedTrait[Typeclass, T]): Typeclass[T] = ???

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]

}