package com.github.cerst.structuredlog4j

import org.apache.logging.log4j.Level
import utest._

import scala.collection.immutable

object LogTests extends TestSuite {

  // logging does not constrain its cause/ message at all so just a simple randomization to "prove" it is not static
  private val message: String = "Message at " + System.currentTimeMillis()
  private val cause = new RuntimeException("Exception at " + System.currentTimeMillis())

  private def test(loggerLevel: Level, op: Log => Unit, expected: immutable.Queue[TestLog4jLogger.Entry]): Unit = {

    val testLog4jLogger = new TestLog4jLogger(loggerLevel)
    val log = new Log(testLog4jLogger)

    op(log)

    val actuallyLogged = testLog4jLogger.logged
    assert(actuallyLogged == expected)
  }

  override val tests: Tests = Tests {

    // =================================================================================================================
    // DEBUG
    // =================================================================================================================
    "debug - logged (same logger level) - empty MDC" - {
      test(
        Level.DEBUG,
        _.debug(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.DEBUG, message, immutable.Map.empty))
      )
    }

    "debug - logged (same logger level) - NON-empty MDC" - {
      test(
        Level.DEBUG,
        _.debug(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.DEBUG, message, immutable.Map("foo" -> "bar")))
      )
    }

    "debug - logged (higher logger level) - empty MDC" - {
      test(
        Level.TRACE,
        _.debug(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.DEBUG, message, immutable.Map.empty))
      )
    }

    "debug - logged (higher logger level) - NON-empty MDC" - {
      test(
        Level.TRACE,
        _.debug(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.DEBUG, message, immutable.Map("foo" -> "bar")))
      )
    }

    "debug - NOT logged (lower log level) - empty MDC" - {
      test(Level.INFO, _.debug(message), immutable.Queue.empty)
    }

    "debug - NOT logged (lower log level) - NON-empty MDC" - {
      test(Level.INFO, _.debug(message, "foo" -> "bar"), immutable.Queue.empty)
    }

    // =================================================================================================================
    // INFO
    // =================================================================================================================
    "info - logged (same logger level) - empty MDC" - {
      test(
        Level.INFO,
        _.info(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.INFO, message, immutable.Map.empty))
      )
    }

    "info - logged (same logger level) - NON-empty MDC" - {
      test(
        Level.INFO,
        _.info(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.INFO, message, immutable.Map("foo" -> "bar")))
      )
    }

    "info - logged (higher logger level) - empty MDC" - {
      test(
        Level.DEBUG,
        _.info(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.INFO, message, immutable.Map.empty))
      )
    }

    "info - logged (higher logger level) - NON-empty MDC" - {
      test(
        Level.DEBUG,
        _.info(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.INFO, message, immutable.Map("foo" -> "bar")))
      )
    }

    "info - NOT logged (lower log level) - empty MDC" - {
      test(Level.WARN, _.info(message), immutable.Queue.empty)
    }

    "info - NOT logged (lower log level) - NON-empty MDC" - {
      test(Level.WARN, _.info(message, "foo" -> "bar"), immutable.Queue.empty)
    }

    // =================================================================================================================
    // WARN (MM)
    // =================================================================================================================
    "warn - logged (same logger level) - empty MDC" - {
      test(
        Level.WARN,
        _.warn(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.WARN, message, immutable.Map.empty))
      )
    }

    "warn - logged (same logger level) - NON-empty MDC" - {
      test(
        Level.WARN,
        _.warn(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.WARN, message, immutable.Map("foo" -> "bar")))
      )
    }

    "warn - logged (higher logger level) - empty MDC" - {
      test(
        Level.INFO,
        _.warn(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.WARN, message, immutable.Map.empty))
      )
    }

    "warn - logged (higher logger level) - NON-empty MDC" - {
      test(
        Level.INFO,
        _.warn(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.WARN, message, immutable.Map("foo" -> "bar")))
      )
    }

    "warn - NOT logged (lower log level) - empty MDC" - {
      test(Level.ERROR, _.warn(message), immutable.Queue.empty)
    }

    "warn - NOT logged (lower log level) - NON-empty MDC" - {
      test(Level.ERROR, _.warn(message, "foo" -> "bar"), immutable.Queue.empty)
    }

    // =================================================================================================================
    // WARN (CMM)
    // =================================================================================================================
    "warn - logged (same logger level) - empty MDC" - {
      test(
        Level.WARN,
        _.warn(cause, message),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.WARN, message, immutable.Map.empty))
      )
    }

    "warn - logged (same logger level) - NON-empty MDC" - {
      test(
        Level.WARN,
        _.warn(cause, message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.WARN, message, immutable.Map("foo" -> "bar")))
      )
    }

    "warn - logged (higher logger level) - empty MDC" - {
      test(
        Level.INFO,
        _.warn(cause, message),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.WARN, message, immutable.Map.empty))
      )
    }

    "warn - logged (higher logger level) - NON-empty MDC" - {
      test(
        Level.INFO,
        _.warn(cause, message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.WARN, message, immutable.Map("foo" -> "bar")))
      )
    }

    "warn - NOT logged (lower log level) - empty MDC" - {
      test(Level.ERROR, _.warn(cause, message), immutable.Queue.empty)
    }

    "warn - NOT logged (lower log level) - NON-empty MDC" - {
      test(Level.ERROR, _.warn(cause, message, "foo" -> "bar"), immutable.Queue.empty)
    }

    // =================================================================================================================
    // ERROR (MM)
    // =================================================================================================================
    "error - logged (same logger level) - empty MDC" - {
      test(
        Level.ERROR,
        _.error(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.ERROR, message, immutable.Map.empty))
      )
    }

    "error - logged (same logger level) - NON-empty MDC" - {
      test(
        Level.ERROR,
        _.error(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.ERROR, message, immutable.Map("foo" -> "bar")))
      )
    }

    "error - logged (higher logger level) - empty MDC" - {
      test(
        Level.WARN,
        _.error(message),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.ERROR, message, immutable.Map.empty))
      )
    }

    "error - logged (higher logger level) - NON-empty MDC" - {
      test(
        Level.WARN,
        _.error(message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(None, Level.ERROR, message, immutable.Map("foo" -> "bar")))
      )
    }

    "error - NOT logged (lower log level) - empty MDC" - {
      test(Level.FATAL, _.error(message), immutable.Queue.empty)
    }

    "error - NOT logged (lower log level) - NON-empty MDC" - {
      test(Level.FATAL, _.error(message, "foo" -> "bar"), immutable.Queue.empty)
    }

    // =================================================================================================================
    // ERROR (CMM)
    // =================================================================================================================
    "error - logged (same logger level) - empty MDC" - {
      test(
        Level.ERROR,
        _.error(cause, message),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.ERROR, message, immutable.Map.empty))
      )
    }

    "error - logged (same logger level) - NON-empty MDC" - {
      test(
        Level.ERROR,
        _.error(cause, message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.ERROR, message, immutable.Map("foo" -> "bar")))
      )
    }

    "error - logged (higher logger level) - empty MDC" - {
      test(
        Level.WARN,
        _.error(cause, message),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.ERROR, message, immutable.Map.empty))
      )
    }

    "error - logged (higher logger level) - NON-empty MDC" - {
      test(
        Level.WARN,
        _.error(cause, message, "foo" -> "bar"),
        immutable.Queue(TestLog4jLogger.Entry(Some(cause), Level.ERROR, message, immutable.Map("foo" -> "bar")))
      )
    }

    "error - NOT logged (lower log level) - empty MDC" - {
      test(Level.FATAL, _.error(cause, message), immutable.Queue.empty)
    }

    "error - NOT logged (lower log level) - NON-empty MDC" - {
      test(Level.FATAL, _.error(cause, message, "foo" -> "bar"), immutable.Queue.empty)
    }

  }

}
