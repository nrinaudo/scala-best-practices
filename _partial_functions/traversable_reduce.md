---
title: Avoid using reduce
layout: article
linters:
  - name: wartremover
    rules:
      - name: TraversableOps
        url:  http://www.wartremover.org/doc/warts.html#traversableops
---

> When reducing a collection to a single value, prefer [`reduceOption`] to [`reduce`].

# Reason

Some collections are empty, and [`reduce`] deals with them by throwing an exception:

```scala
Seq.empty[Int].reduce(_ + _)
// java.lang.UnsupportedOperationException: empty.reduceLeft
//   at scala.collection.LinearSeqOptimized.reduceLeft(LinearSeqOptimized.scala:139)
//   at scala.collection.LinearSeqOptimized.reduceLeft$(LinearSeqOptimized.scala:138)
//   at scala.collection.immutable.List.reduceLeft(List.scala:89)
//   at scala.collection.TraversableOnce.reduce(TraversableOnce.scala:211)
//   at scala.collection.TraversableOnce.reduce$(TraversableOnce.scala:211)
//   at scala.collection.AbstractTraversable.reduce(Traversable.scala:108)
//   ... 43 elided
```

[`reduceOption`] is a safer alternative, since it encodes the possibility of the empty list in its return type:

```scala
Seq(1, 2, 3).reduceOption(_ + _)
// res1: Option[Int] = Some(6)

Seq.empty[Int].reduceOption(_ + _)
// res2: Option[Int] = None
```

[`reduce`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#reduce[A1%3E:A](op:(A1,A1)=%3EA1):A1
[`reduceOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#reduceOption[A1%3E:A](op:(A1,A1)=%3EA1):Option[A1]
