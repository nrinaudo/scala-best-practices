---
title: Arrays should be compared with sameElements
layout: article
section: best_practice
category: collections
---

> When comparing two arrays for equality, use [`sameElements`] rather than `==`.

# Reason

`==` does not compare arrays for value equality, but for reference equality.

Given a simple [`Array`]:

```tut:silent
val as = Array(1)
```

Using `==` to compare will not yield the intuitive result:

```tut:book
as == Array(1)
```

This is because [`Array`] is essentially an alias for Java's array, which implements [`equals`] as reference equality - only returning `true` if two variables contain the same array instance.

[`sameElements`], on the other hand, as the behaviour you'd expect:

```tut:book
as.sameElements(Array(1))
```

# Alternatives

An alternative to [`sameElements`] is calling [`deep`] on each array before comparison:

```tut:book
Array(1).deep == Array(1).deep
```

This is not the preferred solution because it's slightly more expensive, creating instances that will be discarded immediately.

[`Array`]:https://www.scala-lang.org/api/2.12.8/scala/Array.html
[`sameElements`]:https://www.scala-lang.org/api/2.12.8/scala/Array.html#sameElements(that:scala.collection.GenIterable[A]):Boolean
[`deep`]:https://www.scala-lang.org/api/2.12.8/scala/Array.html#deep:IndexedSeq[Any]
[`equals`]:https://docs.oracle.com/javase/8/docs/api/java/util/Objects.html#equals-java.lang.Object-java.lang.Object-
