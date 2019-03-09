package usage

// #example-generated
import java.time.Instant

import com.github.cerst.structuredlog4j.Log

object ExampleGenerated {

  private val log = Log[Example.type]

  if (log.log4jLogger.isInfoEnabled) {
    import org.apache.logging.log4j.ThreadContext
    ThreadContext.put("timestamp", Instant.now().toString)
    ThreadContext.put("foo", "bar")
    log.info("Example shown")
    ThreadContext.clearAll()
  }

}
// #example-generated
