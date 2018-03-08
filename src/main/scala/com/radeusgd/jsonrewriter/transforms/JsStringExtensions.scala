package com.radeusgd.jsonrewriter.transforms

import spray.json.JsString

class JsStringExtensions(val v: JsString) extends AnyVal {
   def map(f: String => String): JsString = JsString(f(v.value))
}
