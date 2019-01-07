---
title: Algebraic Data Types
layout: definition
---

Algebraic Data Types (ADTs for short) are a way of structuring data. They're widely used in Scala due, mostly, to how well they work with pattern matching and how easy it is to use them to make illegal states impossible to represent.

There are two basic categories of ADTs:
* product types
* sum types

# Product types

A product type is essentially a way of sticking multiple values inside of one - a [`Tuple`], or something that's very similar to one. Case classes are the prototypical product type:

```scala mdoc
final case class Foo(b1: Boolean, b2: Boolean)
```

`Foo` aggregates two [`Boolean`] values.

It's called a _product_ type because we can compute its arity (the number of values it can possibly have) by calculating the product of the types that compose it.

Here, [`Boolean`] has an arity of 2 (it can only contain either `true` or `false`), and thus `Foo` must have an arity of 4. Indeed:
* `Foo(true, true)`
* `Foo(true, false)`
* `Foo(false, true)`
* `Foo(false, false)`

# Sum types

A sum type is a type that is composed of different possible values and value shapes. The simplest possible example is an enumeration - `Bool`, for example:

```scala mdoc
sealed abstract class Bool extends Product with Serializable

object Bool {
  final case object True extends Bool
  final case object False extends Bool
}
```

It's called a _sum_ type because its arity is equal to the sum of the arities of the types that compose it. Here, both `True` and `False` are singleton types, and `Bool` can indeed only have 2 possible values.

Sum types get a lot more interesting when you start using more complex data types for the alternatives.

# Bringing them together

Let's imagine a very simple language in which you can only give the following instructions:
* `move forward X meters`
* `rotate Y degrees`

A naïve implementation could be:

```scala mdoc
final case class Command(label: String, meters: Option[Int], degrees: Option[Int])
```

This is problematic, however, since it allows so many illegal states to be represented. For example:

```scala mdoc:silent
Command("foo", None, None)
Command("bar", Some(1), Some(2))
```

By reworking our type to a slightly more involved ADT, we get rid of these:

```scala mdoc:reset
sealed abstract class Command extends Product with Serializable

object Command {
  final case class Move(meters: Int) extends Command
  final case class Rotate(degrees: Int) extends Command
}
```

It's now impossible to create a value that makes no sense - either you move forward by X meters, or you rotate by Y degrees, nothing else.

This type also has the advantage of being very pattern match friendly:

```scala mdoc
def print(cmd: Command) = cmd match {
  case Command.Move(dist)    => println(s"Moving by ${dist}m")
  case Command.Rotate(angle) => println(s"Rotating by ${angle}°")
}
```

[`Tuple`]:https://www.scala-lang.org/api/2.12.8/scala/Tuple2.html
[`Boolean`]:https://www.scala-lang.org/api/2.12.8/scala/Boolean.html
