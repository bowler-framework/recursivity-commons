package com.recursivity.commons.bean

/**
 * Implementations transform/looks up from a String to an Option of type Option[T], or "None" if a transformation is not possible.
 * Useful for instance as an indirection for retrieving objects from a persistent store by ID.
 */

trait StringValueTransformer[T]{
  def toValue(from: String): Option[T]
}