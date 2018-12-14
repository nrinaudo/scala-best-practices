---
title: Avoid using last
layout: article
section: best_practice
category: collections
---

> When you need to retrieve the last element of a sequence, use [`lastOption`] rather than [`last`].

# Reason

Some collections are empty, and [`last`] deals with this by throwing an exception:

```tut:book:fail
Seq.empty[Int].last
```

[`lastOption`] is a safer alternative, since it encodes the possibility of the empty list in the return type:

```tut:book
Seq(1, 2, 3).lastOption

Seq.empty[Int].lastOption
```

[`last`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#last:A
[`lastOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#lastOption:Option[A]
