package com.radeusgd.jsonrewriter.transforms

import spray.json.{JsArray, JsValue}

class JsArrayExtensions(val v: JsArray) extends AnyVal {
   def map(f: JsValue => JsValue): JsArray = JsArray(v.elements.map(f))
   def flatMap2(f: JsValue => Vector[JsValue]): JsArray = JsArray(v.elements.flatMap(f))
   def flatMap(f: JsValue => JsArray): JsArray = flatMap2(f(_).elements)

   def filter(p: JsValue => Boolean): JsArray = JsArray(v.elements.filter(p))
}
