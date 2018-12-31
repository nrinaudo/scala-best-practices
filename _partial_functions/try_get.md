---
title: Do not call get on an Try
layout: article
---

> When retrieving the content of a [`Try`], do not use [`get`].

# Reason

Some [`Trys`][`Try`] are [`Failures`][`Failure`], and [`get`] deals with them by throwing an exception.

```scala
scala.util.Failure(new Exception).get
// java.lang.Exception
//   ... 43 elided
```

If you have a default value to provide in case of a [`Failure`], use [`getOrElse`]:

```scala
scala.util.Failure(new Exception).getOrElse(-1)
// res1: Int = -1
```

Another practical approach is to use [`fold`], which lets you provide a handler for each case:

```scala
import scala.util.{Failure, Try}
// import scala.util.{Failure, Try}

(Failure(new Exception): Try[Int]).fold(
  e => s"Found an error: '${e.getMessage}'",
  i => s"Found an int: '$i'"
)
// res2: String = Found an error: 'null'
```

[`Try`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html
[`getOrElse`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html#getOrElse[U%3E:T](default:=%3EU):U
[`fold`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html#fold[U](fa:Throwable=%3EU,fb:T=%3EU):U
[`get`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html#get:T
[`Failure`]:https://www.scala-lang.org/api/2.12.8/scala/util/Failure.html
[`Success`]:https://www.scala-lang.org/api/2.12.8/scala/util/Success.html
