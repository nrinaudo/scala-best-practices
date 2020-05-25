---
title: Do not concatenate Strings with +
layout: article
linters:
  - name: wartremover
    rules:
      - name: StringPlusAny
        url:  http://www.wartremover.org/doc/warts.html#stringplusany
  - name: scalafix
    rules:
      - name: Disable.ifSynthetic = [ "scala.Predef.any2stringadd" ]
        url: https://github.com/vovapolu/scaluzzi
---

> When concatenating something to a [`String`], consider string interpolation rather than [`+`].

# Reason

Because of a little gem called [`any2stringadd`], [`+`] is just not very sane in Scala.

It has, for example, wildly different behaviours for similar collections:

```scala mdoc
List("foo") + "bar"

Set("foo") + "bar"
```

[`+`] also causes this kind of absolutely nonsensical error message:

```scala mdoc:fail
List(1) + 2
```

This is because of my old nemesis, [implicit conversions](../unsafe/implicit_conversions.html). What the compiler actually ends up trying is:

```scala mdoc:fail:silent
any2stringadd(List(1)).+(2)
```

It makes sense when you understand the underlying mechanisms, but good luck explaining that to a beginner.

String interpolation, on the other hand, is safe and coherent:

```scala mdoc
s"${List("foo")}bar"

s"${Set("foo")}bar"

s"${List(1)}${2}"
```

[`+`]:https://www.scala-lang.org/api/2.12.8/scala/Any.html#+(other:String):String
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
[`any2stringadd`]:https://www.scala-lang.org/api/2.12.8/scala/Predef$.html#any2stringadd[A]extendsAnyVal
