---
title: Sealed types
layout: definition
---

Marking a type as `sealed` means that it can only have direct subtypes that are declared in the same source file.

For example, declaring this in, say, `Foo.scala`, will compile:

```scala mdoc
sealed class Foo

class Bar extends Foo
```

But the following, in `Baz.scala`, will not:

```scala mdoc
class Baz extends Foo
```

This is useful when all subtypes of a given type are known and we want to let the compiler know about it - typically, when working with [ADTs](adt.html).
