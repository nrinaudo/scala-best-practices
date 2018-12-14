---
title: Do not compute the size of a collection to check for emptiness
layout: article
section: best_practice
category: collections
---

> When checking whether a collection is empty, use [`isEmpty`] or [`nonEmpty`] rather than the all too common `size == 0`.

# Reason

First, this is inefficient for collections such as [`List`], which require a complete traversal in order to compute the size.

Second, some collections are infinite, and will loop forever when you try to compute their length:

[//]:I can't use tut here, since this will loop forever
```scala
// Don't run this.
Stream.from(1).size
```

Instead, use the two methods that have been designed precisely for that purpose: [`isEmpty`] and [`nonEmpty`].

```tut:book
Stream.from(1).isEmpty
```

[`List`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html
[`Stream`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/Stream.html
[`isEmpty`]:https://www.scala-lang.org/api/2.12.8/scala/collection/SeqLike.html#isEmpty:Boolean
[`nonEmpty`]:https://www.scala-lang.org/api/2.12.8/scala/collection/SeqLike.html#nonEmpty:Boolean
