package com.github.cerst.structuredlog4j

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.async.AsyncLoggerContext
import utest._

object ConfigTests extends TestSuite {

  override def tests: Tests = Tests {

    "(all-) async loggers is configured" - {
      val actualLoggerContext = LogManager.getContext
      assert(actualLoggerContext.isInstanceOf[AsyncLoggerContext])
    }

  }
}
