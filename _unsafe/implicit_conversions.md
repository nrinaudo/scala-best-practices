---
title: Avoid implicit conversions
layout: article
linters:
  - name: wartremover
    rules:
      - name: ImplicitConversion
        url:  http://www.wartremover.org/doc/warts.html#implicitconversion
---

> Avoid implicit conversions like the plague they are.

# Reason

Implicit conversions allow the compiler to treat values of a type as values of another type.

There's at least one set of scenarios in which this is unambiguously bad: non-[total] conversions. That is, converting an `A` to a `B` when there exists `A`s for which this conversion is impossible.

For example, [`String`] to [`Int`]:

```scala
implicit def str2int(str: String): Int = Integer.parseInt(str)
```

What this does is give the compiler a proof, as far as it's concerned, that all [`String`]s are [`Int`]s and he can accept the former where the later is expected.

Our proof is unfortunately flawed, and will result in runtime failures:

```scala
"foobar" / 2
// java.lang.NumberFormatException: For input string: "foobar"
// 	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
// 	at java.base/java.lang.Integer.parseInt(Integer.java:652)
// 	at java.base/java.lang.Integer.parseInt(Integer.java:770)
// 	at repl.Session$App$.str2int(implicit_conversions.md:8)
// 	at repl.Session$App$$anonfun$1.apply$mcI$sp(implicit_conversions.md:15)
// 	at repl.Session$App$$anonfun$1.apply(implicit_conversions.md:15)
// 	at repl.Session$App$$anonfun$1.apply(implicit_conversions.md:15)
```

# Exceptions to the rule

[Total][total] conversions are more palatable - they shouldn't result in runtime failures, at least.

One common scenario is adding methods to an existing type:

```scala
implicit class ExtendedInt(i: Int) {
  def add1: Int = i + 1
}
```

Which lets us run:

```scala
1.add1
// res0: Int = 2
```

In this scenario, all [`Int`]s can be converted to `ExtendedInt`: the conversion is [total] and cannot result in runtime failure.

[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
[total]:../definitions/total_function.html

