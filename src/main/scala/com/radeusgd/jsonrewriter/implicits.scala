package com.radeusgd.jsonrewriter

import com.radeusgd.jsonrewriter.transforms.JsObjectExtensions
import spray.json.JsObject

import scala.language.implicitConversions

object implicits {
   implicit final def jsObjectExtension(o: JsObject): JsObjectExtensions = new JsObjectExtensions(o)
}
