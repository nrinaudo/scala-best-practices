---
title: Reference equality
layout: definition
---

Reference equality is an equality relationship that does not only guarantee that two values are equal, but also that they are, in fact, the same value.

The distinction might not be obvious. It's possible to have two values that are equivalent - you can use one or the other to get the exact same result - but not the same. For example:

```scala mdoc:silent
val foo = Some(1)
val bar = foo
val baz = Some(1)
```

`foo`, `bar` and `baz` are all equal:

```scala mdoc
foo == bar

foo == baz

bar == baz
```

On the other hand, while `foo` and `bar` are the same value, `baz` isn't:

```scala mdoc
foo eq bar

foo eq baz

bar eq baz
```

Reference equality is rarely used, except when [warning people against it](../unsafe/array_comparison.html).
