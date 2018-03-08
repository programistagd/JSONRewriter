package com.radeusgd.jsonrewriter.example

//import java.io.File

import com.radeusgd.jsonrewriter.IO
import spray.json._
import com.radeusgd.jsonrewriter.implicits._

object Example {
   def transformJson(r: JsValue): JsValue = {
      r.asJsObject.mapDescendant("a.b.c", (v: JsValue) => v.asJsNumber.map(_ + 1))
         .rename("a", "root")
   }

   def main(args: Array[String]): Unit = {
      /*if (args.length != 2) {
         println("Usage: TODO [input] [output]")
      } else {
         val in = args(0)
         val out = args(1)

         IO.processFileLineByLine(transformJson, new File(in), new File(out))
      }*/

      val exampleText =
         """
           |{"a":{"b":{"c":3}}}
           |{"a":{"b":{"c":30}}}
           |{"a":{"b":{"c":5}}}
         """.stripMargin

      IO.processLines(transformJson, exampleText.lines).foreach(println)
   }
}
