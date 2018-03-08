package com.radeusgd.jsonrewriter.transforms

import spray.json.{JsArray, JsNumber, JsString, JsValue}

class JsValueExtensions(val v: JsValue) extends AnyVal {

   def asJsNumber: JsNumber = v.asInstanceOf[JsNumber]

   def asNumber: BigDecimal = asJsNumber.value

   def asInt: Int = asNumber.toInt

   def asJsString: JsString = v.asInstanceOf[JsString]

   def asString: String = asJsString.value

   def asJsArray: JsArray = v.asInstanceOf[JsArray]

   def asArray: Vector[JsValue] = asJsArray.elements
}
