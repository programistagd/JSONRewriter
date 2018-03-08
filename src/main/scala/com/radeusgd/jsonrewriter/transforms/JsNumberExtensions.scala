package com.radeusgd.jsonrewriter.transforms

import spray.json.{JsNumber, JsValue}

class JsNumberExtensions(val v: JsNumber) extends AnyVal {
   def map(f: BigDecimal => BigDecimal): JsNumber = JsNumber(f(v.value))
   def flatMap(f: BigDecimal => JsValue): JsValue = f(v.value)
}
