package com.radeusgd.jsonrewriter.transforms

import spray.json.{JsArray, JsValue}

class JsArrayExtensions(val v: JsArray) extends AnyVal {
   def map(f: JsValue => JsValue): JsArray = JsArray(v.elements.map(f))
   def flatMap(f: JsValue => Vector[JsValue]): JsArray = JsArray(v.elements.flatMap(f))
   def flatMap(f: JsValue => JsArray): JsArray = flatMap(f(_).elements)
}
