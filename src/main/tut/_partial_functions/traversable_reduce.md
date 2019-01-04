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

```tut:fail:book
Seq.empty[Int].reduce(_ + _)
```

[`reduceOption`] is a safer alternative, since it encodes the possibility of the empty list in its return type:

```tut:book
Seq(1, 2, 3).reduceOption(_ + _)

Seq.empty[Int].reduceOption(_ + _)
```

[`reduce`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#reduce[A1%3E:A](op:(A1,A1)=%3EA1):A1
[`reduceOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#reduceOption[A1%3E:A](op:(A1,A1)=%3EA1):Option[A1]
