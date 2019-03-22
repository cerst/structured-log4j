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

import scala.reflect.macros.blackbox

private[structuredlog4j] object LogMacro {

  type LogContext = blackbox.Context { type PrefixType = Log }

  def debug(c: LogContext)(message: c.Expr[String], mdc: c.Expr[(String, String)]*): c.universe.Tree = {
    import c.universe._
    val log4jLogger = q"${c.prefix}.log4jLogger"
    val isEnabled = q"$log4jLogger.isDebugEnabled"
    val log = q"$log4jLogger.debug($message)"
    logIfEnabled(c)(isEnabled, log, mdc)
  }

  def info(c: LogContext)(message: c.Expr[String], mdc: c.Expr[(String, String)]*): c.universe.Tree = {
    import c.universe._
    val log4jLogger = q"${c.prefix}.log4jLogger"
    val isEnabled = q"$log4jLogger.isInfoEnabled"
    val log = q"$log4jLogger.info($message)"
    logIfEnabled(c)(isEnabled, log, mdc)
  }

  def warnMM(c: LogContext)(message: c.Expr[String], mdc: c.Expr[(String, String)]*): c.universe.Tree = {
    import c.universe._
    val log4jLogger = q"${c.prefix}.log4jLogger"
    val isEnabled = q"$log4jLogger.isWarnEnabled"
    val log = q"$log4jLogger.warn($message)"
    logIfEnabled(c)(isEnabled, log, mdc)
  }

  def warnCMM(
    c: LogContext
  )(cause: c.Expr[Throwable], message: c.Expr[String], mdc: c.Expr[(String, String)]*): c.universe.Tree = {
    import c.universe._
    val log4jLogger = q"${c.prefix}.log4jLogger"
    val isEnabled = q"$log4jLogger.isWarnEnabled"
    val log = q"$log4jLogger.warn($message, $cause)"
    logIfEnabled(c)(isEnabled, log, mdc)
  }

  def errorMM(c: LogContext)(message: c.Expr[String], mdc: c.Expr[(String, String)]*): c.universe.Tree = {
    import c.universe._
    val log4jLogger = q"${c.prefix}.log4jLogger"
    val isEnabled = q"$log4jLogger.isErrorEnabled"
    val log = q"$log4jLogger.error($message)"
    logIfEnabled(c)(isEnabled, log, mdc)
  }

  def errorCMM(
    c: LogContext
  )(cause: c.Expr[Throwable], message: c.Expr[String], mdc: c.Expr[(String, String)]*): c.universe.Tree = {
    import c.universe._
    val log4jLogger = q"${c.prefix}.log4jLogger"
    val isEnabled = q"$log4jLogger.isErrorEnabled"
    val log = q"$log4jLogger.error($message, $cause)"
    logIfEnabled(c)(isEnabled, log, mdc)
  }

  private def logIfEnabled(
    c: LogContext
  )(isEnabled: c.universe.Tree, log: c.universe.Tree, mdc: Seq[c.Expr[(String, String)]]): c.universe.Tree = {
    import c.universe._

    // translate the mdc vararg into ThreadContext.put statements
    // don't try to resolve 'seq: _*' within the macro - if there is a need to pass in Seq (or Map), provide a dedicated method instead
    val threadContextPutOps = mdc.map(_.tree match {
      // matches a '"key" -> "value"' mdc-entry
      case q"scala.Predef.ArrowAssoc[$_]($key).->[$_]($value)" =>
        q"ThreadContext.put($key, $value)"
      // matches a '("key", "value")' mdc-entry
      case q"scala.Tuple2.apply[$_, $_]($key, $value)" =>
        q"ThreadContext.put($key, $value)"
      case otherTree =>
        c.abort(
          otherTree.pos,
          s"""Unsupported mdc (varargs) entry expression '$otherTree'. Use ArrowAssoc (e.g. "foo" -> "bar" ) or Tuple2 (e.g. ("foo", "bar") )"""
        )
    })
    q"""if ($isEnabled) {
          import org.apache.logging.log4j.ThreadContext
          ..$threadContextPutOps
          $log
          ThreadContext.clearAll()
        }
     """
  }

}
