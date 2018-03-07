package com.radeusgd.jsonrewriter.example

//import java.io.File

import com.radeusgd.jsonrewriter.IO
import spray.json._
import com.radeusgd.jsonrewriter.implicits._

object Example {
   def transformJson(v: JsValue): JsValue = {
      v.asJsObject.transformedPath("a.b.c", (_: JsValue) => JsNumber(0))
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
           |{"a":{"b":{"c":"d"}}}
           |{"a":{"b":{"c":30}}}
           |{"a":{"b":{"c":null}}}
         """.stripMargin

      IO.processLines(transformJson, exampleText.lines).foreach(println)
   }
}
