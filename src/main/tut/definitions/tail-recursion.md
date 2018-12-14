---
title: Tail recursion
layout: article
---

A tail recursive function is a [recursive] function where the recursive call is the last thing to happen. Our `sum` is not tail recursive, because once it calls itself, it takes the result and adds it to the list's head.

To make it tail recursive, we use an accumulator:

```tut:silent
def sum(is: List[Int]): Int = {
  def loop(cur: List[Int], acc: Int): Int = cur match {
    case h :: t ⇒ loop(t, acc + h)
    case _      ⇒ acc
  }
  loop(is, 0)
}
```

`sum` itself is not tail recursive - it's not even recursive anymore. On the other hand, `loop` is tail recursive: you can see that its last action is to call itself.

[recursive]:./recursion.html
