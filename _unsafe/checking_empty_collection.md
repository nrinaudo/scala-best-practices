---
title: Do not compute the size of a collection to check for emptiness
layout: article
linters:
  - name: scapegoat
    rules:
      - name: AvoidSizeEqualsZero
      - name: AvoidSizeNotEqualsZero
---

> When checking whether a collection is empty, use [`isEmpty`] or [`nonEmpty`] rather than the all too common `size == 0`.

# Reasons

## It's inefficient

Collections such as [`List`] require a complete traversal to compute their size. Doing so just to check whether there's at least one element is clearly more expensive than it needs to be.

To prove this point, let's create a simple benchmarking method:

```scala
def time[A](t: => A): Long = {
  val now = System.currentTimeMillis()
  t
  System.currentTimeMillis() - now
}
```

We'll also need an unreasonably large [`List`]:

```scala
val list = (0 to 10000000).toList
```

We can now check that one approach is indeed far longer than the other:

```scala
time(list.isEmpty)
// res0: Long = 0L

time(list.size == 0)
// res1: Long = 49L
```

## It's unsafe

Some collections are infinite, and will loop forever when you try to compute their length:

[//]:I can't use mdoc here, since this will loop forever

```scala
// Don't run this.
Stream.from(1).size
```

[`isEmpty`], on the other hand, behaves sanely:

```scala
Stream.from(1).isEmpty
// res2: Boolean = false
```

[`List`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html
[`Stream`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/Stream.html
[`isEmpty`]:https://www.scala-lang.org/api/2.12.8/scala/collection/SeqLike.html#isEmpty:Boolean
[`nonEmpty`]:https://www.scala-lang.org/api/2.12.8/scala/collection/SeqLike.html#nonEmpty:Boolean

