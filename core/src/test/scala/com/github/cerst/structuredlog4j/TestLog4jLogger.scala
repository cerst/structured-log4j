/*
 * Copyright (c) 2019 Constantin Gerstberger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.cerst.structuredlog4j

import org.apache.logging.log4j.message.Message
import org.apache.logging.log4j.spi.AbstractLogger
import org.apache.logging.log4j.{Level, Marker, ThreadContext}

import scala.collection.immutable

object TestLog4jLogger {

  final case class Entry(cause: Option[Throwable], level: Level, message: String, mdc: Map[String, String])
}

/**
  * Test-implementation storing log calls in a local queue to be asserted (rather than e.g. printing to std-out).
  */
final class TestLog4jLogger(val level: Level, var logged: immutable.Seq[TestLog4jLogger.Entry] = immutable.Queue.empty)
    extends AbstractLogger {

  import TestLog4jLogger.Entry

  override def isEnabled(level: Level, marker: Marker, message: Message, t: Throwable): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: CharSequence, t: Throwable): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: Any, t: Throwable): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String, t: Throwable): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String, params: AnyRef*): Boolean = {
    lazy val _ = (level, marker, message, params)
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String, p0: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String, p0: Any, p1: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String, p0: Any, p1: Any, p2: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level, marker: Marker, message: String, p0: Any, p1: Any, p2: Any, p3: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level,
                         marker: Marker,
                         message: String,
                         p0: Any,
                         p1: Any,
                         p2: Any,
                         p3: Any,
                         p4: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level,
                         marker: Marker,
                         message: String,
                         p0: Any,
                         p1: Any,
                         p2: Any,
                         p3: Any,
                         p4: Any,
                         p5: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level,
                         marker: Marker,
                         message: String,
                         p0: Any,
                         p1: Any,
                         p2: Any,
                         p3: Any,
                         p4: Any,
                         p5: Any,
                         p6: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level,
                         marker: Marker,
                         message: String,
                         p0: Any,
                         p1: Any,
                         p2: Any,
                         p3: Any,
                         p4: Any,
                         p5: Any,
                         p6: Any,
                         p7: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level,
                         marker: Marker,
                         message: String,
                         p0: Any,
                         p1: Any,
                         p2: Any,
                         p3: Any,
                         p4: Any,
                         p5: Any,
                         p6: Any,
                         p7: Any,
                         p8: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def isEnabled(level: Level,
                         marker: Marker,
                         message: String,
                         p0: Any,
                         p1: Any,
                         p2: Any,
                         p3: Any,
                         p4: Any,
                         p5: Any,
                         p6: Any,
                         p7: Any,
                         p8: Any,
                         p9: Any): Boolean = {
    this.level.intLevel() >= level.intLevel()
  }

  override def logMessage(fqcn: String, level: Level, marker: Marker, message: Message, t: Throwable): Unit = {
    // parameters are not expected to be used for formatting, so use getFormattedMessage to prove this
    var mdc = immutable.Map.empty[String, String]
    ThreadContext.getImmutableContext forEach { (key, value) =>
      mdc += (key -> value)
    }
    logged :+= Entry(Option(t), level, message.getFormattedMessage, mdc)
    ()
  }

  override def getLevel: Level = level
}
