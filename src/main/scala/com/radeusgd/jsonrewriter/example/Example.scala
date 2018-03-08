package com.radeusgd.jsonrewriter.example

import com.radeusgd.jsonrewriter.IO
import com.radeusgd.jsonrewriter.implicits._
import com.radeusgd.jsonrewriter.functions._
import spray.json._

object Example {
   def main(args: Array[String]): Unit = {

      println("[Example 1] Convert some formats")

      val exampleText =
         """
           |{"user":{"age": 30, "names": "Jack Oliver Jake", "lastname": "King"}, "profile": {"adclicks": [0,1,2,3]}}
           |{"user":{"age": 45, "names": "Emma Amelia Margaret", "lastname": "Smith"}, "profile": {"adclicks": [0,1]}}
           |{"user":{"age": 26, "names": "Thomas", "lastname": "Martin"}, "profile": {"adclicks": []}}
         """.stripMargin

      val convert: JsValue => JsValue =
         asJsObject _ |> compose[JsObject](
            _.mapChild("user", _.asJsObject.rename("lastname", "surname")), // rename property
            _.mapDescendant("user.age", (j: JsValue) => j.asJsNumber.map(_ * 365)), // estimate age in days
            _.mapDescendant("user.names", (j: JsValue) => // convert names to array of strings
               JsArray(j.asString.split(' ').toVector.map(JsString(_))))
         )

      IO.processLines(convert, exampleText.lines).foreach(println)
      println()

      println("[Example 2] Group keys into objects")

      val exampleText2 =
         """
           |{"name": "John", "mother_name": "Emma", "mother_age": 35, "father_name": "Thomas", "father_age": 40}
           |{"name": "Emma", "mother_name": "Olivia", "mother_age": 30, "father_name": "Jack", "father_age": 29}
         """.stripMargin

      val groupParents: JsValue => JsValue =
         asJsObject _ |> compose[JsObject](
            _.groupAndRename(Map("mother_name" -> "name", "mother_age" -> "age"), "mother"),
            _.groupAndRename(Map("father_name" -> "name", "father_age" -> "age"), "father")
         )

      IO.processLines(groupParents, exampleText2.lines).foreach(println)
      println()

      println("[Example 3] Group keys into an array")

      val exampleText3 =
         """
           |{"bikeid": "RC4963", "owner1": "Peter", "owner2": "Amelia", "owner3": "Jack", "owner4": "John"}
           |{"bikeid": "RK2528", "owner1": "Thomas", "owner2": null, "owner3": "Jack", "owner4": "Margaret", "repairs": 4}
           |{"bikeid": "RC4694", "owner1": null, "owner2": null, "owner3": null, "owner4": null}
         """.stripMargin

      val arrayize: JsValue => JsValue =
         asJsObject _ |> compose[JsObject](
            _.mapChildrenToArray(Vector("owner1", "owner2", "owner3", "owner4"), "owners"),
            _.mapChild("owners", _.asJsArray.filter(_ != JsNull)) // throw away empty values
         )

      IO.processLines(arrayize, exampleText3.lines).foreach(println)
      println()
   }
}
