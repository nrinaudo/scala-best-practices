---
title: Avoid using head
layout: article
---

> When retrieving the head of a sequence, use [`headOption`] rather than [`head`].

# Reason

Some collections are empty, and [`head`] deals with them by throwing an exception:

```tut:book:fail
Seq.empty[Int].head
```

[`headOption`] is a safer alternative, since it encodes the possibility of the empty list in its return type:

```tut:book
Seq(1, 2, 3).headOption

Seq.empty[Int].headOption
```

[`head`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#head:A
[`headOption`]:https://www.scala-lang.org/api/2.12.8/scala/collection/Seq.html#headOption:Option[A]
