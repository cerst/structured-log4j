# structured-log4j

Opinionated, macro-powered Scala wrapper of Log4j (2).  

Opinionated because

* all-asynchronous logging is enabled by default
* the library provides a replacement API focused on _structured logging_

For the purpose of this library, _structured logging_ implies conveying logging context in way that allows for it to be 
properly handled in downstream log aggregator/ processors such as _Elasticsearch.  
To that end, all logging parameters are put into log4j's _message diagnostic context (MDC) a.k.a. ThreadContext_ rather 
than the log message string (see @ref:[Usage](usage/index.md) for details).

@@@ index

* [Changelog](changelog/index.md)
* [Licenses](licenses/index.md)
* [Usage](usage/index.md)

@@@
