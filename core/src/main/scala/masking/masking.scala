
import java.util.UUID

import cats.Show
import magnolia.{CaseClass, Magnolia, SealedTrait}
import shapeless.{::, HList, HNil}

import scala.language.experimental.macros
import scala.annotation.Annotation

package object masking {
  class masked extends Annotation
  class semimasked extends Annotation

  implicit val UuidShow: Show[UUID] = (uuid: UUID) => uuid.toString
  implicit val stringShow: Show[String] = (str: String) => str
  implicit val intShow: Show[Int] = (int: Int) => int.toString
  implicit val longShow: Show[Long] = (long: Long) => long.toString
  implicit val floatShow: Show[Float] = (float: Float) => float.toString
  implicit val doubleShow: Show[Double] = (double: Double) => double.toString

  implicit def hconsShow[H, T <: HList](implicit H: Show[H], T: Show[T]): Show[H :: T] = (a: H :: T) => T.show(a.tail) match {
    case empty if empty.isEmpty => H.show(a.head)
    case _ => H.show(a.head) + "\n" + T.show(a.tail)
  }

  implicit val hnilShow: Show[HNil] = (_: HNil) => ""

  type Typeclass[T] = Show[T]

  def combine[T](ctx: CaseClass[Show, T]): Show[T] = (value: T) => ctx.parameters.map {
    case p if p.annotations.exists {
      case _: masked => true
      case _ => false
    } => s"${p.label}=${masks.masked(p.dereference(value))}"

    case p if p.annotations.exists {
      case _: semimasked => true
      case _ => false
    } => s"${p.label}=${masks.semiMasked(p.dereference(value))}"

    case p => s"${p.label}=${p.typeclass.show(p.dereference(value))}"

  }.mkString(s"${ctx.typeName.short}(", ", ", ")")

  def dispatch[T](ctx: SealedTrait[Show, T]): Show[T] =
    (value: T) => ctx.dispatch(value) { sub =>
      sub.typeclass.show(sub.cast(value))
    }

  implicit def gen[T]: Show[T] = macro Magnolia.gen[T]
}
