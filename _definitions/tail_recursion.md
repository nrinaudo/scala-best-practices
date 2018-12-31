---
title: Tail recursion
layout: article
---

A tail recursive function is a [recursive] function where the recursive call is the last thing to happen.

The following `sum` method is [recursive], but not tail recursive:

```scala
def sum(is: List[Int]): Int = is match {
  case h :: t => h + sum(t)
  case _      => 0
}
```

In the first `case`, `sum` calls itself *then* adds the result to the head of the list.

To make it tail recursive, we use an accumulator:

```scala
def sum(is: List[Int]): Int = {
  def loop(cur: List[Int], acc: Int): Int = cur match {
    case h :: t => loop(t, acc + h)
    case _      => acc
  }
  loop(is, 0)
}
```

`sum` itself is not tail recursive - it's not even recursive anymore. On the other hand, `loop` is tail recursive: you can see that its last action is to call itself.

[recursive]:./recursion.html
