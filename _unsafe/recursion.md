---
title: Make recursive functions tail-recursive
layout: article
---

> When writing [recursive] functions, make them [tail recursive].

# Reason

Here's a naïve recursive function used to compute the sum of a [`List[Int]`][`List`]:

```scala
def sum(is: List[Int]): Int = is match {
  case h :: t => h + sum(t)
  case _      => 0
}
```

This implementation suffers from a major flaw: on long enough lists, it will cause a runtime exception.

[//]: I cannot use tut here since there's no way I could find to truncate the output, and the stack trace is *large*.
```scala
sum(List.range(1, 30000))
// java.lang.StackOverflowError
//   at .sum(<console>:12)
//   at .sum(<console>:13)
//   at .sum(<console>:13)
//   [...]
```

What happens is that, for each recursive call, the call's context must be stored on the stack - and when a recursive function calls itself a lot, you get a stack overflow exception.

When the compiler spots a [tail recursive] function, however, it knows to optimise it by essentially turning it into a `while` loops - no more recursive calls, no more frames pushed on the stack.

Here's our `sum` function, but implemented tail recursively:

```scala
def sum(is: List[Int]): Int = {

  // `loop` i our tail recursive function, keeping a running sum
  // in `acc`
  def loop(cur: List[Int], acc: Int): Int = cur match {
    case h :: t => loop(t, acc + h)
    case _      => acc
  }

  loop(is, 0)
}
```

And as you can see, this is now safe:

```scala
sum(List.range(1, 30000))
// res0: Int = 449985000
```

[recursive]:../definitions/recursion.html
[tail recursive]:../definitions/tail_recursion.html
[`List`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html
