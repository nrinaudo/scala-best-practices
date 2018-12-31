---
title: Recursion
layout: article
---

A recursive function is one that calls itself. For example:

```scala
def sum(is: List[Int]): Int = is match {
  case h :: t => h + sum(t)
  case _      => 0
}
```

`sum` is implemented by referencing itself: the sum of a non empty list is the sum of its head plus the sum of its tail.
