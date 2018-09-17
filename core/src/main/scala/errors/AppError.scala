package errors

trait AppError extends Throwable {
  override def getMessage: String = "Something went wrong"
}

case class InputError(field: String, msg: String) extends AppError {
  override def getMessage: String = s"Input error in $field: $msg"
}