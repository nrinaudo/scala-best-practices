---
title: Total functions
layout: definition
---

A function is said to be total if it's defined for it's entire domain. That is, `A => B` is total if there is a `B` for every possible `A`.

A function that is not total is said to be [partial](https://www.scala-lang.org/api/2.12.8/scala/PartialFunction.html).

For example, in the following code:

```scala
def int2str(i: Int): String = i.toString

def str2int(s: String): Int = Integer.parseInt(s)
```

* `int2str` is total because there exists no [`Int`] for which it's not defined.
* `str2int` is partial because there exists *many* [`Strings`][`String`] for which it isn't defined.

[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
