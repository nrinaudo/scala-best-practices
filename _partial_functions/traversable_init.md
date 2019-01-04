---
title: Avoid using init
layout: article
linters:
  - name: wartremover
    rules:
      - name: TraversableOps
        url:  http://www.wartremover.org/doc/warts.html#traversableops
---

> When retrieving everything but the last element of a sequence, do not use [`init`]. [`dropRight(1)`] is often what you want to use.

# Reason

Some collections are empty, and [`init`] deals with them by throwing an exception:

```scala
Seq.empty[Int].init
// java.lang.UnsupportedOperationException: empty.init
//   at scala.collection.TraversableLike.init(TraversableLike.scala:454)
//   at scala.collection.TraversableLike.init$(TraversableLike.scala:453)
//   at scala.collection.AbstractTraversable.init(Traversable.scala:108)
//   ... 43 elided
```

[`dropRight(1)`], on the other hand, will yield a reasonable value: the empty list.

```scala
Seq(1, 2, 3).dropRight(1)
// res1: Seq[Int] = List(1, 2)

Seq.empty[Int].dropRight(1)
// res2: Seq[Int] = List()
```

# Exceptions to the rule

Note that this is not *always* what you want to do. There are scenarios in which you must deal with the empty case explicitly - as a stop condition in a recursive function, say.
It's just that, often, getting the empty list when you ask for everything but the last element of an empty list is perfectly reasonable.

[`dropRight(1)`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#dropRight(n:Int):Repr
[`init`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#init:Repr
