---
title: Avoid using tail
layout: article
---

> When retrieving everything but the first element of a sequence, do not use [`tail`]. [`drop(1)`] is often what you want to use.

# Reason

Some collections are empty, and [`tail`] deals with them by throwing an exception:

```scala
Seq.empty[Int].tail
// java.lang.UnsupportedOperationException: tail of empty list
//   at scala.collection.immutable.Nil$.tail(List.scala:432)
//   at scala.collection.immutable.Nil$.tail(List.scala:427)
//   ... 43 elided
```

[`drop(1)`], on the other hand, will yield a reasonable value: the empty list.

```scala
Seq(1, 2, 3).drop(1)
// res1: Seq[Int] = List(2, 3)

Seq.empty[Int].drop(1)
// res2: Seq[Int] = List()
```

# Exceptions to the rule

Note that this is not *always* what you want to do. There are scenarios in which you must deal with the empty case explicitly - as a stop condition in a recursive function, say.
It's just that, often, getting the empty list when you ask for everything but the first element of an empty list is perfectly reasonable.

[`tail`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#tail:A
[`drop(1)`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#drop(n:Int):Repr
