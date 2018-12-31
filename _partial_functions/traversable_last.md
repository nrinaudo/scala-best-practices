---
title: Avoid using last
layout: article
---

> When retrieving the last element of a sequence, use [`lastOption`] rather than [`last`].

# Reason

Some collections are empty, and [`last`] deals with them by throwing an exception:

```scala
Seq.empty[Int].last
// java.util.NoSuchElementException
//   at scala.collection.LinearSeqOptimized.last(LinearSeqOptimized.scala:150)
//   at scala.collection.LinearSeqOptimized.last$(LinearSeqOptimized.scala:149)
//   at scala.collection.immutable.List.last(List.scala:89)
//   ... 43 elided
```

[`lastOption`] is a safer alternative, since it encodes the possibility of the empty list in its return type:

```scala
Seq(1, 2, 3).lastOption
// res1: Option[Int] = Some(3)

Seq.empty[Int].lastOption
// res2: Option[Int] = None
```

[`last`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#last:A
[`lastOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#lastOption:Option[A]
