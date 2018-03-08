package com.radeusgd.jsonrewriter.transforms

import spray.json.{JsString, JsValue}

class JsStringExtensions(val v: JsString) extends AnyVal {
   def map(f: String => String): JsString = JsString(f(v.value))
   def flatMap(f: String => JsValue): JsValue = f(v.value)
}
