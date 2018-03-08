package com.radeusgd.jsonrewriter

import com.radeusgd.jsonrewriter.transforms._
import spray.json._

import scala.language.implicitConversions

object implicits {
   implicit final def jsObjectExtension(o: JsObject): JsObjectExtensions = new JsObjectExtensions(o)
   implicit final def jsValueExtension(v: JsValue): JsValueExtensions = new JsValueExtensions(v)
   implicit final def jsArrayExtension(v: JsArray): JsArrayExtensions = new JsArrayExtensions(v)
   implicit final def jsStringExtension(v: JsString): JsStringExtensions = new JsStringExtensions(v)
   implicit final def jsNumberExtension(v: JsNumber): JsNumberExtensions = new JsNumberExtensions(v)
}
