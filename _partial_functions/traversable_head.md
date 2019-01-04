---
title: Avoid using head
layout: article
linters:
  - name: wartremover
    rules:
      - name: TraversableOps
        url:  http://www.wartremover.org/doc/warts.html#traversableops
  - name: scapegoat
    rules:
      - name: TraversableHead
---

> When retrieving the head of a sequence, use [`headOption`] rather than [`head`].

# Reason

Some collections are empty, and [`head`] deals with them by throwing an exception:

```scala
Seq.empty[Int].head
// java.util.NoSuchElementException: head of empty list
//   at scala.collection.immutable.Nil$.head(List.scala:430)
//   at scala.collection.immutable.Nil$.head(List.scala:427)
//   ... 43 elided
```

[`headOption`] is a safer alternative, since it encodes the possibility of the empty list in its return type:

```scala
Seq(1, 2, 3).headOption
// res1: Option[Int] = Some(1)

Seq.empty[Int].headOption
// res2: Option[Int] = None
```

[`head`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#head:A
[`headOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#headOption:Option[A]
