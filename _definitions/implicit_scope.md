---
title: Implicit scope
layout: article
---

The implicit scope of a type `T` is where the compiler will look when attempting to locate implicit instances for that type. It is composed of all the companion objects of types associated with `T`.

To take a concrete example:

```scala
class Foo

object Foo {
  implicit val bar: List[Foo] = List.empty
}
```

`bar` is of type [`List[Foo]`][`List`], and is located within the companion object of `Foo`, a type associated with [`List[Foo]`][`List`]: it's in the implicit scope of [`List[Foo]`][`List`] and we need no special import for the compiler to locate it.

[//]: Cannot tut this, since `object Foo` isn't actually the companion object of `Foo` in a repl session.
```scala
implicitly[List[Foo]]
```

The implicit scope is particularly helpful when defining [type class](type-class.html) instances.

[`List`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html
