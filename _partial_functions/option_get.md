---
title: Do not call get on an Option
layout: article
linters:
  - name: wartremover
    rules:
      - name: OptionPartial
        url:  http://www.wartremover.org/doc/warts.html#optionpartial
  - name: scapegoat
    rules:
      - name: OptionGet
---

> When retrieving the content of an [`Option`], do not use [`get`].

# Reason

Some [`Options`][`Option`] are [`Nones`][`None`], and [`get`] deals with them by throwing an exception:

```scala
None.get
// java.util.NoSuchElementException: None.get
// 	at scala.None$.get(Option.scala:366)
// 	at repl.Session$App$$anonfun$1.apply(option_get.md:9)
// 	at repl.Session$App$$anonfun$1.apply(option_get.md:9)
```

If you have a default value to provide for the [`None`] case, use [`getOrElse`]:

```scala
None.getOrElse(-1)
// res0: Int = -1
```

Another practical approach is to use [`fold`], which lets you provide a handler for each case:

```scala
(None: Option[String]).fold("Found None")(i => s"Found an int: '$i'")
// res1: String = "Found None"
```

[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
[`None`]:https://www.scala-lang.org/api/2.12.8/scala/None$.html
[`get`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html#get:A
[`getOrElse`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html#getOrElse[B%3E:A](default:=%3EB):B
[`fold`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html#fold[A1%3E:A](z:A1)(op:(A1,A1)=%3EA1):A1

