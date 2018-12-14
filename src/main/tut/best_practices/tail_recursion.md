---
title: Tail-recursive functions should be flagged as such
layout: article
section: best_practice
category: safety
---

> Always annotate your [tail-recursive] function with [`@annotation.tailrec`].

# Reason

This will let the compiler know that you expect your function to be [tail-recursive], and allow compilation to fail should it not be.

This is important, because there are scenarios where you'd expect a function to be [tail-recursive] but it really isn't. Take, for example:

```tut:silent
class Foo {
  def sum(cur: List[Int], acc: Int): Int = cur match {
    case h :: t ⇒ sum(t, acc + h)
    case _      ⇒ acc
  }
}
```

This looks [tail-recursive] - `sum` calls itself in tail position, after all. Turns out, however, that it's not:

```tut:book:fail
class Foo {
  @annotation.tailrec
  def sum(cur: List[Int], acc: Int): Int = cur match {
    case h :: t ⇒ sum(t, acc + h)
    case _      ⇒ acc
  }
}
```

Thanks to that error message, we can fix the problem and make `sum` propery [tail-recursive]:

```tut:silent
class Foo {
  @annotation.tailrec
  final def sum(cur: List[Int], acc: Int): Int = cur match {
    case h :: t ⇒ sum(t, acc + h)
    case _      ⇒ acc
  }
}
```

[recursive]:../definitions/recursion.html
[tail-recursive]:../definitions/tail-recursion.html
[`@annotation.tailrec`]:https://www.scala-lang.org/api/2.12.8/scala/annotation/tailrec.html
