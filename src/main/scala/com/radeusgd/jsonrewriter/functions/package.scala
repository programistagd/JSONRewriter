package com.radeusgd.jsonrewriter

import spray.json._
import com.radeusgd.jsonrewriter.implicits._

package object functions {
   /*
   Composes endomorphisms, the order of functions in the list is the same as the order in which they are applied (head is the first one etc.)
    */
   def compose[T](fs: (T => T)*): T => T =
      fs.foldLeft(identity[T] _)(_.andThen(_))

   implicit class PipelineOperator[A,B](val f: A => B) extends AnyVal {
      def `|>`[C](g: B => C): A => C = f.andThen(g)
   }

   def asJsObject(j: JsValue): JsObject = j.asJsObject
   def asJsArray(j: JsValue): JsArray = j.asJsArray
   def asJsString(j: JsValue): JsString = j.asJsString
   def asJsNumber(j: JsValue): JsNumber = j.asJsNumber

}
