package com.radeusgd.jsonrewriter.transforms

import spray.json.{JsObject, JsValue}

class JsObjectExtensions(val o: JsObject) extends AnyVal {
   def without(key: String): JsObject = JsObject(o.fields.filter({ case (k, _) => k != key }))
   def updated(key: String, value: JsValue): JsObject = JsObject(o.fields.updated(key, value))

   // this function assumes that key is present
   def transformed(key: String, transform: JsValue => JsValue): JsObject = updated(key, transform(o.fields(key)))

   def transformedPath(path: List[String], transform: JsValue => JsValue): JsObject = path match {
      case Nil => o // empty path is identity
      case h :: Nil => transformed(h, transform)
      case h :: t => transformed(h, (v: JsValue) => new JsObjectExtensions(v.asJsObject).transformedPath(t, transform))
   }

   def transformedPath(path: String, transform: JsValue => JsValue): JsObject = transformedPath(path.split('.').toList, transform)
}
