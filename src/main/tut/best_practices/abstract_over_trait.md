---
title: Abstract classes should be used instead of traits
layout: article
section: best_practice
category: data_types
---

> When declaring an abstract type and you have a choice between an abstract class and a trait, use an abstract class.

This usually can be thought of as _when writing an [ADT](../definitions/adt.html), have the root type be an abstract class_, because you can't really rule out the possibility that your abstract type might eventually be involved in multipe inheritance in most other scenarios.

# Reasons

## Binary compatibility

Adding a new concrete method to an existing trait breaks [binary compatibility](../definitions/binary-compatibility.html), which is a big deal for libraries.

## Java interrop

Traits can be a bit painful when you're writing Java code that has Scala dependencies. One concrete example of this is companion objects:

```tut:silent
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
