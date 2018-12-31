---
title: Prefer abstract classes to traits
layout: article
---

> When declaring an abstract type without a clear reason to prefer a `trait`, use an `abstract class`.

Since in most scenarios that aren't declaring an [ADT](../definitions/adt.html), it's impossible to guarantee that your type will never be involved in multiple inheritance, this rule can mostly be simplified to:

> When writing an [ADT](../definitions/adt.html), have the root type be an abstract class

# Reasons

## Binary compatibility

Adding a new concrete method to an existing trait breaks [binary compatibility](../definitions/binary_compatibility.html), which is a big deal for libraries.

The reason this happens has [been explained](https://stackoverflow.com/questions/18366817/is-adding-a-trait-method-with-implementation-breaking-backward-compatibility) far better than I could hope to do.

## Java interop

Traits can be a bit painful when you're writing Java code that has Scala dependencies. One concrete example of this is companion objects:

```scala
trait ATrait

object ATrait {
  def foo(): Int = 1
}

abstract class AnAbtractClass

object AnAbstractClass {
  def foo(): Int = 2
}
```

With these definitions, calling `foo` from Java code will look like:

```java
ATrait$.MODULE$.foo()

AnAbstractClass.foo()
```

Clearly, the abstract class version is more natural.

## It's a good default

The previous reasons don't really matter to projects that are not libraries or will never have to be used from Java code.

But abstract classes are not a worse choice than traits in most other scenarios (again, except when multiple inheritance is a possibility), and I prefer:

> When in doubt, use an abstract class.

to

> When in doubt and writing code that is or might become a library or is or might be called from Java, use an abstract class.
