---
title: Avoid using init
layout: article
section: best_practice
category: collections
---

> When you need to retrieve everything but the last element of a sequence, do not use [`init`]. [`dropRight(1)`] is often what you want to use.

# Reason

Some collections are empty, and [`init`] deals with this by throwing an exception:

```tut:book:fail
Seq.empty[Int].init
```

[`dropRight(1)`], on the other hand, will yield a reasonable value: the empty list.

```tut:book
Seq(1, 2, 3).dropRight(1)

Seq.empty[Int].dropRight(1)
```

# Exceptions to the rule

Note that this is not *always* what you want to do. There are scenarios in which you must deal with the empty case explicitly - as a stop condition in a recursive function, say:

```tut:silent
// This is quite possibly the worse way size has ever been implemented.
def badSize[A](as: Seq[A]): Int = {
  if(as.nonEmpty) 1 + badSize(as.init)
  else            0
}
```

Re-implementing that with [`dropRight(1)`] would loop infinitely:

```tut:silent
def worseSize[A](as: Seq[A]): Int = 1 + worseSize(as.dropRight(1))
```


It's just that, often, getting the empty list when you ask for everything but the last element of an empty list is perfectly reasonable.

[`dropRight(1)`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#dropRight(n:Int):Repr
[`init`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#init:Repr
