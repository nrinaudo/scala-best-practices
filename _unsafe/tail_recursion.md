---
title: Mark tail-recursive functions as such
layout: article
---

> Always annotate your [tail-recursive] function with [`@annotation.tailrec`].

# Reason

This will let the compiler know that you expect your function to be [tail-recursive], and allow compilation to fail should it not be.

This is important, because there are scenarios where you'd expect a function to be [tail-recursive] but it really isn't. Take, for example:

```scala
class Foo {
  def sum(cur: List[Int], acc: Int): Int = cur match {
    case h :: t => sum(t, acc + h)
    case _      => acc
  }
}
```

This looks [tail-recursive] - `sum` calls itself in tail position, after all. Turns out, however, that it's not:

```scala
class Foo {
  @annotation.tailrec
  def sum(cur: List[Int], acc: Int): Int = cur match {
    case h :: t => sum(t, acc + h)
    case _      => acc
  }
}
// <console>:14: error: could not optimize @tailrec annotated method sum: it is neither private nor final so can be overridden
//          def sum(cur: List[Int], acc: Int): Int = cur match {
//              ^
```

Thanks to that error message, we can fix the problem and make `sum` propery [tail-recursive]:

```scala
class Foo {
  @annotation.tailrec
  final def sum(cur: List[Int], acc: Int): Int = cur match {
    case h :: t => sum(t, acc + h)
    case _      => acc
  }
}
```

[recursive]:../definitions/recursion.html
[tail-recursive]:../definitions/tail_recursion.html
[`@annotation.tailrec`]:https://www.scala-lang.org/api/2.12.8/scala/annotation/tailrec.html
