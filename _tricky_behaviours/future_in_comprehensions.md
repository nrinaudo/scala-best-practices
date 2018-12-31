---
title: Start independent Futures outside of a for-comprehension
layout: article
---

> When working with independent [`Futures`][`Future`], make sure not to initialise them inside a for-comprehension.

# Reason

For-comprehension will create a dependency between your [`Futures`][`Future`], turning your code synchronous behind your back.

To understand how this can happen, it's important to realise that for-comprehensions are just syntactic sugar for nested [`flatMap`] and [`map`] calls.

Take the following code:

```scala
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

def longRunning(): Int = {
  Thread.sleep(100)
  println("LONG")
  1
}

def immediate(): Int = {
  println("IMMEDIATE")
  2
}

def combine(): Future[Int] = for {
  i <- Future(longRunning())
  j <- Future(immediate())
} yield i + j
```

`combine` desugars to:

```scala
def combine(): Future[Int] =
  Future(longRunning()).flatMap { i =>
    Future(immediate()).map(j => i + j)
  }
```

This means that the second [`Future`] (the one that yields `2`) cannot be started before the first one has completed - `i` is in its scope, even if not used - even though these two [`Futures`][`Future`] are clearly independent from each other.

To make this evident, let's evaluate `combine`. However many times you run it, `LONG` will always be printed before `IMMEDIATE`, even though the former is executed after a significant delay:

```scala
Await.result(combine(), 500.millis)
// LONG
// IMMEDIATE
// res0: Int = 3
```

This can be worked around by creating the two [`Future`] instances outside of the for-comprehension: [`Future`] has the controversial behaviour that it starts executing when created, not when evaluated.

```scala
def betterCombine(): Future[Int] = {
  val f1 = Future(longRunning())
  val f2 = Future(immediate())

  for {
    i <- f1
    j <- f2
  } yield i + j
}
```

And if we now evaluate `betterCombine`, the log messages should print in the expected order:

```scala
Await.result(betterCombine(), 200.millis)
// IMMEDIATE
// LONG
// res1: Int = 3
```

[`Future`]:https://www.scala-lang.org/api/2.12.8/scala/concurrent/Future.html
[`flatMap`]:https://www.scala-lang.org/api/2.12.8/scala/concurrent/Future.html#flatMap[S](f:T=%3Escala.concurrent.Future[S])(implicitexecutor:scala.concurrent.ExecutionContext):scala.concurrent.Future[S]
[`map`]:https://www.scala-lang.org/api/2.12.8/scala/concurrent/Future.html#map[S](f:T=%3ES)(implicitexecutor:scala.concurrent.ExecutionContext):scala.concurrent.Future[S]
