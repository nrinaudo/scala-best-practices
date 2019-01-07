---
title: Do not return Option from custom extractors
layout: article
---

> When defining a custom extractor that happens to be [total](../definitions/total_function.html), always have a return type of [`Some`] rather than [`Option`].

# Reason

This is a bit convoluted and has to do with exhaustivity checking in pattern matching, by which I mean:

```scala
def unwrap(oi: Option[Int]): Int = oi match {
  case Some(i) => i
}
// warning: match may not be exhaustive.
// It would fail on the following input: None
```

You get a warning here because there are values of `oi` that would cause your code to fail at runtime.

A common misconception about this check is that you get a warning whenever the compiler fails to prove exhaustivity. What actually happens is slightly different: you'll get a warning whenever the compiler succeeds in proving non-exhaustivity.

The distinction is subtle, but important: when the compiler cannot find a concrete value that is not covered by a pattern match, even though it knows such a value might exist, it will not warn.

And if your custom extractor returns [`Option`], that's exactly what happens: it's telling the compiler that there might be values for which it's not defined, but it doesn't tell it *which* values. This effectively disables exhaustivity checking.

Here's a concrete example:

```scala
// Generic error ADT, encoding both known and unknown errors.
sealed abstract class Error extends Product with Serializable

object Error {
  final case class Unknown(msg: String) extends Error

  sealed abstract class Known(val code: Int) extends Error
  object Known {
    // Convenient for pattern matching, lets us extract the code
    // of any known error.
    def unapply(known: Known): Option[Int] = Some(known.code)

    final case object NotFound extends Known(404)
    final case object BadRequest extends Known(400)
  }
}
```

Using this code, we can write the following pattern match:

```scala
def getCode(error: Error): Int = error match {
  case Error.Known(code) => code
}
```

We're not getting a warning, even though our pattern match is clearly non-exhaustive: it'll fail on any `Error.Unknown` value.

```scala
getCode(Error.Unknown("error"))
// scala.MatchError: Unknown(error) (of class repl.Session$App$Error$Unknown)
// 	at repl.Session$App$.getCode(custom_extractors.md:35)
// 	at repl.Session$App$$anonfun$2.apply$mcI$sp(custom_extractors.md:44)
// 	at repl.Session$App$$anonfun$2.apply(custom_extractors.md:44)
// 	at repl.Session$App$$anonfun$2.apply(custom_extractors.md:44)
```

The presence of an extractor whose return type was [`Option`] disabled exhaustivity checking, even in the presence of concrete values that are not covered by the pattern match.

Let's change our custom extractor to return a [`Some`]:

```scala
sealed abstract class Error extends Product with Serializable

object Error {
  final case class Unknown(msg: String) extends Error

  sealed abstract class Known(val code: Int) extends Error
  object Known {
    def unapply(known: Known): Some[Int] = Some(known.code)

    final case object NotFound extends Known(404)
    final case object BadRequest extends Known(400)
  }
}
```

This is a subtle difference that changes everything: [`Some`] means that our extractor will always succeed, allowing the compiler to look for further proof of non-exhaustivity (and find it):

```scala
def getCode(error: Error): Int = error match {
  case Error.Known(code) => code
}
// warning: match may not be exhaustive.
// It would fail on the following input: Unknown(_)
// def getCode(error: Error): Int = error match {
//                                  ^^^^^
```

# Improvement

An even better rule of thumb would be to either not define custom extractors, or at least never define partial ones - pattern match exhaustivity is something that most people take for granted, and sneakily disabling it can lead to all sorts of unpleasant runtime behaviours.

[`Some`]:https://www.scala-lang.org/api/2.12.8/scala/Some.html
[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html

