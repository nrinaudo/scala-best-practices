---
title: Avoid using last
layout: article
linters:
  - name: wartremover
    rules:
      - name: TraversableOps
        url:  http://www.wartremover.org/doc/warts.html#traversableops
  - name: scapegoat
    rules:
      - name: TraversableLast
---

> When retrieving the last element of a sequence, use [`lastOption`] rather than [`last`].

# Reason

Some collections are empty, and [`last`] deals with them by throwing an exception:

```scala mdoc:crash
Seq.empty[Int].last
```

[`lastOption`] is a safer alternative, since it encodes the possibility of the empty list in its return type:

```scala mdoc
Seq(1, 2, 3).lastOption

Seq.empty[Int].lastOption
```

[`last`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#last:A
[`lastOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#lastOption:Option[A]
