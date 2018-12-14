---
title: Implicit names should be as unique as possible
layout: article
section: best_practice
category: safety
---

> When declaring an implicit value or method, try to give it a name that minimises the chances of conflicting with other libraries.

# Reason

Implicit names shadow each other and confuse the compiler (and developers).

For example:

```tut:silent
object foo {
  implicit val bar: Int = 1
}

object baz {
  implicit val bar: String = "foo"
}
```

Note how both `foo` and `baz` contain a value called `bar`, even though they do not share a type and only one is implicit.

This yields the following, confusing behaviour:

```tut:book:fail
{
  import foo._, baz._

  implicitly[Int]
}
```

There's a single implicit value of type [`Int`] in scope - `foo.bar`. But the compiler can't find it.

One way of demonstrating *why* the compiler cannot find it is:

```tut:book:fail
{
  import foo._, baz._

  val a: String = bar
}
```

Even in the presence of sufficient type information (we know that `a` is a [`String`], and only one of the two `bar`s in scope is a [`String`]), the compiler considers two clashing names to be ambiguous and demands the ambiguity fixed.

The problem is easy to understand and fix in this last example - the error message is quite explicit. But in the context of implicit resolution, we're just getting a generic _could not find implicit value_ error message, which can be a bit of a nigthmare to debug.

In order to avoid this, and since you cannot control how other libraries name their implicits, you must:
* give yours very, very unique names - it doesn't matter if they look silly, they're implicits, the idea is to not refer to them by name
* hope that other libraries do the same

# Exceptions to the rule

There's at least one scenario in which an implicit's name isn't used during resolution: when you declare an implicit of type `T` in the implicit scope of `T`.

For example:

```tut:reset:silent
object foo {
  trait Foo[A]
  object Foo {
    implicit val foo: Foo[Int] = new Foo[Int] {}
  }
}

object bar {
  trait Bar[A]
  object Bar {
    implicit val foo: Bar[String] = new Bar[String] {}
  }
}
```

Even though both implicit values share the name `foo`, implicit resolution works out:

```tut:book
{
  import foo._, bar._

  implicitly[Foo[Int]]
}
```

In this scenario, it's safe to give terse names to your implicits.

[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
