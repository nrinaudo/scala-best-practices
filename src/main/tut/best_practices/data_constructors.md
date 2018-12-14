---
title: ADT data constructors should go in the companion object
layout: article
section: best_practice
category: data_types
---

> When writing the various data constructors of an [ADT], put them in the root type's companion object rather than at the top level.

For example, prefer:

```tut:silent
sealed abstract class Option[+A] extends Product with Serializable

object Option {
  final case class Some[A](value: A) extends Option[A]
  final case object None extends Option[Nothing]
}
```

To:

```tut:silent
sealed abstract class Option[+A] extends Product with Serializable

final case class Some[A](value: A) extends Option[A]
final case object None extends Option[Nothing]
```

Yes, I'm aware that this example contradicts the Scala stdlib - that's on purpose. I have [Martin Odersky](https://twitter.com/odersky) on record for recommending exactly the approach that [`Option`] did not take.

# Reason

Putting data constructors in the companion object can be locally made to behave as if they'd been declared at the top level: just import them in.

```tut:silent
sealed abstract class Foo extends Product with Serializable

object Foo {
  final case object Bar extends Foo
  final case object Baz extends Foo
}

{
  // This brings Bar and Baz in the local scope, exactly as if
  // they'd been declared at the top level.
  import Foo._

  println(Bar)
}
```

It's however impossible to get top level data constructors to behave as if they'd been declared in the companion object. It's possible to hide or rename them, but this requires one instruction per data constructor, which is verbose and doesn't really scale with large [ADTs][ADT]:

```tut:silent

object foo {
  sealed abstract class Foo extends Product with Serializable

  final case object Bar extends Foo
  final case object Baz extends Foo
}

{
  // This renames Bar to FooBar and imports Foo and Baz into the
  // local scope.
  import foo.{Bar => FooBar, _}

  println(FooBar)
}
```

And while you might argue that you'd rarely need to do that, names are a surprisingly scarce resource. You *will* eventually get in naming conflicts - how many times have I written error types that conflicted with [`Failure`]? This rule helps minimise such conflicts by namespacing all data constructor.

[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
[`Failure`]:https://www.scala-lang.org/api/2.12.8/scala/util/Failure.html
[ADT]:../definitions/adt.html
