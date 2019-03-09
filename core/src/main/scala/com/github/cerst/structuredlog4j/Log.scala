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
