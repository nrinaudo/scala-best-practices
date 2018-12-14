---
title: Abstract member implementations should be flagged with override
layout: article
section: best_practice
category: data_types
---

> Whether you're overriding a concrete member or implementing an abstract one, use the `override` keyword.

# Reason

This catches some scenarios where code would otherwise compile but misbehave at runtime.

Take the following trait, for example:

```tut:silent
trait Foo[A] {
  // Default implementation
  def foo1(a: A): Int = 1
}
```

And now, an incorrect but valid implementation:

```tut:silent
implicit val fooInt: Foo[Int] = new Foo[Int] {
  // Notice how this is not quite the right name.
  def fool(i: Int) = 2
}
```

This compiles, but is clearly not what the we intended: we wanted to implement `fool` but declared `foo1` instead. Since `fool` has a default implementation, this is valid but returns `1` instead of the expected `2`.

Had we used the `override` keyword, however, the compiler would have caught our error:

```tut:fail:book
implicit val fooInt: Foo[Int] = new Foo[Int] {
  override def fool(i: Int) = 2
}
```

# Alternatives

Another accepted practice is to *never* use `override` unless required by the language.

This comes from the fact that, for a certain subset of the Scala community, overriding (in the sense of redefining an existing method) is a code smell: the compiler can check that you're working with the right types, but not the invariants that are not expressed in the type system. A common example is implementing a `Set` as a subtype of a `Bag` (see [Subtyping, Subclassing, and Trouble with OOP](http://okmij.org/ftp/Computation/Subtyping/) for an in-depth discussion).

Given that premise, the argument is that since overriding is bad, you should treat the use of the `override` keyword like you would an instruction to silence compiler warnings: explicit acknowledgement that you're doing something unsavoury.

This debate would be solved by adding a new `implement` keywords, similar to `override` but only used to flag members that you expect to implement abstract members in a superclass. This doesn't exist however, and I feel the pragmatic approach is to follow the rule that catches bad programs rather than the purely ideological one.
