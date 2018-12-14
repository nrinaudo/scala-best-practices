---
title: Type classes should be used instead of structural types
layout: article
section: best_practice
category: data_types
---

> Whenever a structural type seems like a good solution to a problem, consider using a type class instead.

# Reasons

## Structural types are "slow"

Structural types rely on runtime reflection. Since reflection pushes some type work to the runtime, it's inherently slower than solutions where all this work is done at compile time.

Note that this is unlikely to be a bottle neck except in very specific use cases, and shouldn't be enough to discourage people in scenarios where structural types are clearly an elegant solution.

## Structural types are not always supported

On the other hand, the fact that structural types might fail at runtime for reasons entirely outside of the code author's control probably is a good reason not to use them.

When running on the JVM (as opposed to the scala.js or native backend, say), there's one scenario where structural types will break: reflection is controlled by whatever security manager is running. The default one is quite lenient, but it's possible to configure one that bans reflection altogether.

This will break Scala code in ways that are impossible to validate statically.

## Type classes can express the same thing

Let's imagine that we want the following behaviour:

```tut:silent
def add1(x: { def get: Int }): Int = 1 + x.get
```

`add1` takes any object that has a `get` method, calls it, adds 1 to it and returns that.

For example:

```tut:book
final case class Wrapper(i: Int) {
  def get: Int = i
}

add1(Wrapper(1))
```

It is, however, possible to express the notion of _has a get: Int_ method as a type class:

```tut:silent
trait HasGet[A] {
  def get(a: A): Int
}

// add1 now works with any type on which value you can call get
def add1[A](a: A)(implicit hga: HasGet[A]): Int = hga.get(a) + 1
```

Declaring an instance for `Wrapper` is straightforward:

```tut:silent
implicit val wrap: HasGet[Wrapper] = new HasGet[Wrapper] {
  def get(a: Wrapper) = a.i
}
```

And we can now call `add1` like we wanted:

```tut:book
add1(Wrapper(1))
```

This solution is admitedly more verbose than the structural type alternative. It's also entirely statically verified, cannot fail at runtime, and is probably also slightly faster.
