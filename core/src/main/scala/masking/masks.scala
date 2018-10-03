package masking

object masks {
  def masked[T](value: T): String = value.toString.map(_ => '#')

  def semiMasked[T](value: T): String = {
    value.toString.split("[^a-zA-Z0-9]").map {
      case str if str.length > 4 => str.map(_ => '#')
      case str => str
    }.mkString("-")
  }
}