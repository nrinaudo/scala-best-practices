---
title: Do not compare arrays with ==
layout: article
---

> When comparing two arrays for equality, use [`sameElements`] rather than `==`.

# Reason

`==` does not compare arrays for value equality, but for [reference equality].

This gives us the following counter-intuitive behaviour:

```tut:book
Array(1) == Array(1)
```

This is because [`Array`] is essentially an alias for Java's array, which implements [`equals`] as [reference equality] - only returning `true` if two variables point to the same array instance.

[`sameElements`], on the other hand, has the behaviour you'd expect:

```tut:book
Array(1) sameElements Array(1)
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
[reference equality]:../definitions/reference_equality.html
