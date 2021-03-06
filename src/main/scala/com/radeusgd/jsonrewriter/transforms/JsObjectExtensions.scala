package com.radeusgd.jsonrewriter.transforms

import com.radeusgd.jsonrewriter.implicits._
import spray.json.{JsArray, JsObject, JsValue}

class JsObjectExtensions(val o: JsObject) extends AnyVal {
   def without(key: String): JsObject = JsObject(o.fields.filter({ case (k, _) => k != key }))

   def updated(key: String, value: JsValue): JsObject = JsObject(o.fields.updated(key, value))

   // this function assumes that key is present
   def mapChild(key: String, f: JsValue => JsValue): JsObject = updated(key, f(o.fields(key)))

   def mapDescendant(path: List[String], f: JsValue => JsValue): JsObject = path match {
      case Nil => o // empty path is identity
      case h :: Nil => mapChild(h, f)
      case h :: t => mapChild(h, (v: JsValue) => v.asJsObject.mapDescendant(t, f))
   }

   def mapDescendant(path: String, f: JsValue => JsValue): JsObject = mapDescendant(path.split('.').toList, f)

   /*
   Simultaneously changes all key's names into their values.
   If any values are duplicated the behaviour is undefined.
   For example:
   {"a": 10, "b": 20}.rename("a"->"b","b"->"a") = {"b": 10, "a": 20}
    */
   def rename(nameChanges: Map[String, String]): JsObject = {
      def newKey(key: String): String = nameChanges.getOrElse(key, key) // this returns the mapping or identity if there's no mapping for that key
      JsObject(o.fields.map({ case (key, value) => (newKey(key), value) }))
   }

   def rename(from: String, to: String): JsObject = rename(Map(from -> to))

   def removeKeys(keys: Iterable[String]): JsObject = keys.foldLeft(o)((acc: JsObject, key: String) => acc.without(key))

   def groupAndRename(nameChanges: Map[String, String], groupName: String): JsObject = {
      val grouped = nameChanges.map({ case (key: String, newKey: String) => (newKey, o.fields(key)) })
      removeKeys(nameChanges.keys).updated(groupName, JsObject(grouped))
   }

   def group(names: List[String], groupName: String): JsObject = groupAndRename(names.map(k => (k, k)).toMap, groupName)

   def mapChildrenToArray(children: Vector[String], arrayName: String): JsObject = {
      val array = JsArray(children.map((key: String) => o.fields(key)))
      removeKeys(children).updated(arrayName, array)
   }
}
