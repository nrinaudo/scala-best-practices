---
title: Custom extractors shouldn't return Option
layout: article
section: best_practice
category: data_types
---

> When defining a custom extractor that happens to be total, always have a return type of [`Some`] rather than [`Option`].

# Reason

This is a bit convoluted and has to do with exhaustivity checking in pattern matching, by which I mean:

```tut:book
def unwrap(oi: Option[Int]): Int = oi match {
  case Some(i) ⇒ i
}
```

You get a warning here because there are values of `oi` that would cause your code to fail at runtime.

A common misconception about this check is that you get a warning whenever the compiler fails to prove exhaustivity. What actually happens is slightly different: you'll get a warning whenever the compiler succeeds in proving non-exhaustivity.

The distinction is subtle, but important: when the compiler cannot find a concrete value that is not covered by a pattern match, even though it knows such a value might exist, it will not warn.

And if your custom extractor returns [`Option`], that's exactly what happens: it's telling the compiler that there might be values for which the extractor is not defined, but it doesn't tell it *which* values. This effectively disables exhaustivity checking.

Here's a concrete example:

```tut:silent
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

And now, a pattern match that:
* uses our custom extractor, `Error.Known.unapply`
* is clearly non-exhaustive, since it'll fail on any `Error.Unknown`.

```tut:book
def getCode(error: Error): Int = error match {
  case Error.Known(code) ⇒ code
}
```

This compiles without a warning. It will, however, fail at runtime:

```tut:book:fail
getCode(Error.Unknown("error"))
```

Let's change our custom extractor to return a [`Some`]:

```tut:silent
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

As far as the compiler is concerned, this removes the ambiguity. It now knows that `Error.Known.unapply` will always succeed and will look for further proof of non-exhaustivity:

```tut:book
def getCode(error: Error): Int = error match {
  case Error.Known(code) ⇒ code
}
```

# Improvement

An even better rule of thumb would be to either not define custom extractors, or at least never define partial ones - pattern match exhaustivity is something that most people take for granted, and sneakily disabling it can lead to all sorts of unpleasant runtime behaviours.

[`Some`]:https://www.scala-lang.org/api/2.12.8/scala/Some.html
[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
