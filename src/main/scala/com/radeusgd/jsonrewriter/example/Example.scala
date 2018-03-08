package com.radeusgd.jsonrewriter.example

import com.radeusgd.jsonrewriter.IO
import com.radeusgd.jsonrewriter.implicits._
import com.radeusgd.jsonrewriter.functions._
import spray.json._

object Example {
   val convert: JsValue => JsValue =
      asJsObject _ |> compose[JsObject](
         _.mapChild("user", _.asJsObject.rename("lastname", "surname")), // rename property
         _.mapDescendant("user.age", (j: JsValue) => j.asJsNumber.map(_ * 365)), // estimate age in days
         _.mapDescendant("user.names", (j: JsValue) => // convert names to array of strings
            j.asJsString.flatMap((s: String) => JsArray(s.split(' ').toVector.map(JsString(_)))))
      )


   def main(args: Array[String]): Unit = {
      val exampleText =
         """
           |{"user":{"age": 30, "names": "Jack Oliver Jake", "lastname": "King"}, "profile": {"adclicks": [0,1,2,3]}}
           |{"user":{"age": 45, "names": "Emma Amelia Margaret", "lastname": "Smith"}, "profile": {"adclicks": [0,1]}}
           |{"user":{"age": 26, "names": "Thomas", "lastname": "Martin"}, "profile": {"adclicks": []}}
         """.stripMargin

      IO.processLines(convert, exampleText.lines).foreach(println)
   }
}
