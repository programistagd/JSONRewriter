package com.radeusgd.jsonrewriter.transforms

import spray.json.JsNumber

class JsNumberExtensions(val v: JsNumber) extends AnyVal {
   def map(f: BigDecimal => BigDecimal): JsNumber = JsNumber(f(v.value))
}
