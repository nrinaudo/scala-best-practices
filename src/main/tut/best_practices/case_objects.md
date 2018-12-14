---
title: Case objects should be declared final
layout: article
section: best_practice
category: data_types
---

> When declaring a case object, always make it [`final`].

# Reason

While case objects are effectively [`final`] in Scala - you can't extend them - they are not always flagged as such in the compiled bytecode.

Take the following code, for example:

```tut:silent
class Test {
  case object foo
}
```

When `foo` is not flagged as [`final`], running the class file through `javap -c` yields the following (truncated) output:

```java
public class Test$foo$ implements scala.Product,scala.Serializable {
 // [...]
```

When it's flagged as [`final`], however, we get:

```java
public final class Test$foo$ implements scala.Product,scala.Serializable {
  // [...]
```

To make matters worse, this is not even coherent accross Scala versions - the previous output was obtained with 2.12.7, but 2.11.8, for example, will mark both versions of `Test.foo` as [`final`].

This might not seem like a big deal - as far as `scalac` is concerned, `Test.foo` is [`final`] either way and you won't be able to extend it - not having this information in the bytecode will:
* prevent the JIT from applying some [`final`]-specific optimisations
* potentially cause Java interrop issues, since `javac` will let you extend `Test.foo`.

[`final`]:../definitions/final.html
