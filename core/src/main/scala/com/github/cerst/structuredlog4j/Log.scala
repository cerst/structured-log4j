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

import org.apache.logging.log4j.{LogManager, Logger}

import scala.reflect.ClassTag

/**
  * Extension value class representing the opinionated Log4j wrapper/ API.
  * <p/>
  * No overloads provided to optimize the 'mdc.isEmpty' case as any log call should have respective parameters.
  */
final class Log private[structuredlog4j] (val log4jLogger: Logger) extends AnyVal {

  def debug(message: String, mdc: (String, String)*): Unit = macro LogMacro.debug

  def info(message: String, mdc: (String, String)*): Unit = macro LogMacro.info

  def warn(message: String, mdc: (String, String)*): Unit = macro LogMacro.warnMM

  def warn(cause: Throwable, message: String, mdc: (String, String)*): Unit = macro LogMacro.warnCMM

  def error(message: String, mdc: (String, String)*): Unit = macro LogMacro.errorMM

  def error(cause: Throwable, message: String, mdc: (String, String)*): Unit = macro LogMacro.errorCMM

}

object Log {

  def apply[A]()(implicit classTag: ClassTag[A]): Log = {
    val log4jLogger = LogManager.getLogger(classTag.runtimeClass)
    new Log(log4jLogger)
  }

}
