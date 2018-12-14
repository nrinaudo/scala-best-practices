---
title: Enumerations should be implemented as ADTs
layout: article
section: best_practice
category: data_types
---

> When you need to implement an enumeration, use an [ADT], not [`Enumeration`].

# Reason

[`Enumeration`] suffers from a major flaw: exhaustivity is not checked in pattern matches.

For example:

```tut:silent
object Status extends Enumeration {
  val Ok, Nok = Value
}

def foo(w: Status.Value): Unit = w match {
  case Status.Ok ⇒ println("ok")
}
```

This compiles without a warning but will blow up at runtime:

```tut:book:fail
foo(Status.Nok)
```

Sealed abstract class hierarchies, on the other hand, do not suffer from this issue. Here's the same enumeration implemented as an [ADT]:

```tut:silent
sealed abstract class Status extends Product with Serializable
object Status {
  final case object Ok extends Status
  final case object Nok extends Status
}
```

The compiler now has enough information to check whether your pattern matches are exhaustive:

```tut:book
def foo(w: Status): Unit = w match {
  case Status.Ok ⇒ println("ok")
}
```

[`Enumeration`]:https://www.scala-lang.org/api/2.12.8/scala/Enumeration.html
[ADT]:../definitions/adt.html
