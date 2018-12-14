---
title: Do not use implicit conversions
layout: article
section: best_practice
category: safety
---

> Avoid implicit conversions like the plague they are.

# Reason

Implicit conversions allow the compiler to treat values of a type as values of another type.

There's at least one set of scenarios in which this is unambiguously bad: non-total conversions. That is, converting an `A` to a `B` when there exists `A`s for which this conversion is impossible.

For example, [`String`] to [`Int`]:

```tut:silent
implicit def str2int(str: String): Int = Integer.parseInt(str)
```

What this does is give the compiler a proof, as far as it's concerned, that all [`String`]s are [`Int`]s and he can accept the former where the later is expected.

Our proof is unfortunately flawed, and will result in runtime failures:

```tut:book:fail
"foobar" / 2
```

# Exceptions to the rule

Total conversions are more palatable - they shouldn't result in runtime failures, at least.

One common scenario is adding methods to an existing type:

```tut:silent
implicit class ExtendedInt(i: Int) {
  def add1: Int = i + 1
}
```

Which lets us run:

```tut:book
1.add1
```

In this scenario, all [`Int`]s can be converted to `ExtendedIt`: the conversion is total and cannot result in runtime failure.

[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
