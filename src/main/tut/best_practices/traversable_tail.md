---
title: Avoid using tail
layout: article
section: best_practice
category: collections
---

> When you need to retrieve everything but the first element of a sequence, do not use [`tail`]. [`drop(1)`] is often what you want to use.

# Reason

Some collections are empty, and [`tail`] deals with this by throwing an exception:

```tut:book:fail
Seq.empty[Int].tail
```

[`drop(1)`], on the other hand, will yield a reasonable value: the empty list.

```tut:book
Seq(1, 2, 3).drop(1)

Seq.empty[Int].drop(1)
```

# Exceptions to the rule

Note that this is not *always* what you want to do. There are scenarios in which you must deal with the empty case explicitly - as a stop condition in a recursive function, say:

```tut:silent
def badSize[A](as: Seq[A]): Int = {
  if(as.nonEmpty) 1 + badSize(as.tail)
  else            0
}
```

Re-implementing that with [`drop(1)`] would loop infinitely:

```tut:silent
def worseSize[A](as: Seq[A]): Int = 1 + worseSize(as.drop(1))
```

It's just that, often, getting the empty list when you ask for everything but the first element of an empty list is perfectly reasonable.



[`tail`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#tail:A
[`drop(1)`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#drop(n:Int):Repr
