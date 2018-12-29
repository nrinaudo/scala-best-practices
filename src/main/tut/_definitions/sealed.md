---
title: Sealed types
layout: article
---

Marking a type as `sealed` means that it can only have direct subtypes that are declared in the same source file.

For example, declaring this in, say, `Foo.scala`, will compile:

```tut:silent
sealed class Foo

class Bar extends Foo
```

But the following, in `Baz.scala`, will not:

```tut:silent
class Baz extends Foo
```

This is useful when all subtypes of a given type are known and we want to let the compiler know about it - typically, when working with [ADTs](adt.html).
