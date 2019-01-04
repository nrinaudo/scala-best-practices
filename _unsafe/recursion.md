---
title: Make recursive functions tail-recursive
layout: article
linters:
  - name: wartremover
    rules:
      - name: Recursion
        url:  http://www.wartremover.org/doc/warts.html#recursion
---

> When writing [recursive] functions, consider making them [tail recursive].

# Reason

Non-tail recursive functions that call themselves too often end up throwing [`StackOverflowError`] at runtime.

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

# Exceptions to the rule

Tail recursion is a desirable property, but not an absolute requirement.

There are scenarios where "normal" recursion is more appropriate. A fairly standard example is tree traversal:
* the recursion is bounded to the height of the tree, so there's no real risk of a [`StackOverflowError`].
* tail-recursive functions for tree exploration are far more complicated than non tail-recursive ones.

As is often the case, this rule is more of a default decision, to be overruled when you need to.



[recursive]:../definitions/recursion.html
[tail recursive]:../definitions/tail_recursion.html
[`List`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html
[`StackOverflowError`]:https://docs.oracle.com/javase/8/docs/api/java/lang/StackOverflowError.html
