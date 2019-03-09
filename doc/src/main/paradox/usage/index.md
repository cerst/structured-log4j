# Usage

## Dependency

@@dependency[sbt,Maven,Gradle] { group="$group$" artifact="$name.core$" version="$version$" }

## Example

First, you need a _log4j2.xml_ configuration which can make use of any feature of the underlying _log4j2_ version.  
Here's a very basic one:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="off">
    <Appenders>
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="%m%n%mdc%n%ex%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="warn">
            <AppenderRef ref="STDOUT"/>
        </Root>
    </Loggers>
</Configuration>
```

Then, you can use the library:

@@snip[Example.scala]($root$/src/main/scala/usage/Example.scala) { #example }

On compile, the macro re-writes the log call as follows:

@@snip[Example.scala]($root$/src/main/scala/usage/ExampleGenerated.scala) { #example-generated }

The number of generated _ThreadContext.put_ statements depends on the number of mdc parameters.
 