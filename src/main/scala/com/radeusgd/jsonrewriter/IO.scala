package com.radeusgd.jsonrewriter

import java.io.{File, PrintWriter}

import spray.json._

object IO {
   def processFileLineByLine(lineTransform: JsValue => JsValue, input: File, output: File): Unit = {
      val source = io.Source.fromFile(input)
      val pw = new PrintWriter(output)
      try {
         val lines = source.getLines()
         processLines(lineTransform, lines).foreach(pw.println(_))
      } finally {
         source.close()
         pw.close()
      }
   }

   def processLines(lineTransform: JsValue => JsValue, lines: Iterator[String]): Iterator[String] = {
      val jsons = lines.filter(!_.trim.isEmpty).map(_.parseJson)
      val transformed = jsons.map(lineTransform)
      transformed.map(_.compactPrint)
   }
}
