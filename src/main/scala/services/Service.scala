package services

import io.finch.Endpoint

trait Service[T] {
  def endpoints: Endpoint[T]
}
