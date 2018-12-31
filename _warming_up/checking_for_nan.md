---
title: Use isNaN when checking for NaN
layout: article
---

> When checking whether a number is [`NaN`], use [`isNaN`] rather than `== NaN`.

# Reason

By [specification](https://en.wikipedia.org/wiki/IEEE_754), [`NaN`] is not equal to anything, not even itself:

```scala
Double.NaN == Double.NaN
// res0: Boolean = false
```

Use [`isNaN`] instead, which is meant for just this task:

```scala
Double.NaN.isNaN
// res1: Boolean = true
```

[`NaN`]:https://www.scala-lang.org/api/2.12.8/scala/Double$.html#NaN:Double(NaN)
[`isNaN`]:https://www.scala-lang.org/api/2.12.8/scala/Double.html#isNaN:Boolean
