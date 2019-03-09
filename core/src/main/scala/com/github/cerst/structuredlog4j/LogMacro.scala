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
