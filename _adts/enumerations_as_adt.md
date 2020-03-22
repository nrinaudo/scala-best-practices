---
title: Use ADTs to implement enumerations
layout: article
linters:
  - name: wartremover
    rules:
      - name: Enumeration
        url:  http://www.wartremover.org/doc/warts.html#enumeration
---

> When you need to implement an enumeration, use an [ADT], not [`Enumeration`].

# Reason

[`Enumeration`] suffers from a major flaw: exhaustivity is not checked in pattern matches.

For example:

```scala
object Status extends Enumeration {
  val Ok, Nok = Value
}

def foo(w: Status.Value): Unit = w match {
  case Status.Ok => println("ok")
}
```

This compiles without a warning but will blow up at runtime:

```scala
foo(Status.Nok)
// scala.MatchError: Nok (of class scala.Enumeration$Val)
// 	at repl.Session$App$.foo(enumerations_as_adt.md:13)
// 	at repl.Session$App$$anonfun$1.apply$mcV$sp(enumerations_as_adt.md:22)
// 	at repl.Session$App$$anonfun$1.apply(enumerations_as_adt.md:22)
// 	at repl.Session$App$$anonfun$1.apply(enumerations_as_adt.md:22)
```

[ADTs][ADT], on the other hand, do not suffer from this issue. Here's a better implementation of `Status`:

```scala
sealed abstract class Status extends Product with Serializable
object Status {
  final case object Ok extends Status
  final case object Nok extends Status
}
```

The compiler now has enough information to check whether your pattern matches are exhaustive:

```scala
def foo(w: Status): Unit = w match {
  case Status.Ok => println("ok")
}
// warning: match may not be exhaustive.
// It would fail on the following input: Nok
// def foo(w: Status): Unit = w match {
//                            ^
```

[`Enumeration`]:https://www.scala-lang.org/api/2.12.8/scala/Enumeration.html
[ADT]:../definitions/adt.html

