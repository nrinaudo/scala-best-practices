---
title: Reference equality
layout: definition
---

Reference equality is an equality relationship that does not only guarantee that two values are equal, but also that they are, in fact, the same value.

The distinction might not be obvious. It's possible to have two values that are equivalent - you can use one or the other to get the exact same result - but not the same. For example:

```scala
val foo = Some(1)
val bar = foo
val baz = Some(1)
```

`foo`, `bar` and `baz` are all equal:

```scala
foo == bar
// res0: Boolean = true

foo == baz
// res1: Boolean = true

bar == baz
// res2: Boolean = true
```

On the other hand, while `foo` and `bar` are the same value, `baz` isn't:

```scala
foo eq bar
// res3: Boolean = true

foo eq baz
// res4: Boolean = false

bar eq baz
// res5: Boolean = false
```

Reference equality is rarely used, except when [warning people against it](../unsafe/array_comparison.html).
