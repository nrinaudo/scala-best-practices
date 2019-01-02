---
title: Declare abstract fields as paren-less methods
layout: article
---

> When declaring abstract fields in an abstract class or a trait, it's good practice to declare them as [paren-less] methods.

Prefer:

```scala mdoc
abstract class Foo {
  def bar: Int
}
```

Over:

```scala mdoc:reset
abstract class Foo {
  val bar: Int
}
```

# Reason

Scala allows abstract [paren-less] methods to be implemented as `val`s, but not the other way around.

Given the following:

```scala mdoc
abstract class AsDef {
  def bar: Int
}

abstract class AsVal {
  val bar: Int
}
```

Implementing `AsDef.bar` as a `val` compiles:

```scala mdoc:silent
new AsDef {
  override val bar = 0
}
```

But implementing `AsVal.bar` as a `def` does not:

```scala mdoc:fail
new AsVal {
  override def bar = 0
}
```

Declaring abstract fields as `val`s closes some possibilities, while declaring them as [paren-less] methods has no ill effect.

# Exceptions to the rule

There's at least one known scenario where declaring an abstract field as a `val` is important: path-dependent types, where its important that the field has a concrete type.

Here, for example:

```scala mdoc:silent:reset
trait Foo {
  val bar: String
}

val foo: Foo = new Foo {
  override val bar = "baz"
}
```

It's possible to to refer explicitly to the type of `foo.bar`:

```scala mdoc:silent
val fooBar: foo.bar.type = foo.bar
```

Had we defined `bar` as a `def`:

```scala mdoc:reset:silent
trait Foo {
  def bar: String
}

val foo: Foo = new Foo {
  override val bar = "baz"
}
```

Then we'd get a compilation error attempting to get the type of `foo.bar`:

```scala mdoc:fail
val fooBar: foo.bar.type = foo.bar
```


[paren-less]:../definitions/paren-less.html
