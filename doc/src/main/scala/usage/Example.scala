package usage

// #example
import java.time.Instant

import com.github.cerst.structuredlog4j.Log

object Example {

  private val log = Log[Example.type]

  // don't put parameters into the message (first arg) - use the mdc instead (third/ varargs)
  // debug, warn & error work the same way
  log.info("Example shown", "timestamp" -> Instant.now().toString, "foo" -> "bar")

}
// #example
