---
title: Do not use null
layout: article
section: best_practice
category: safety
---

> Whenever `null` seems like a good idea, use [`Option`] instead.

# Reasons

## It's not safe

As far as types are concerned, `null` is a bit of a lie:

```tut:book
val s: String = null
```

The compiler believes `s` to be a [`String`] and will accept it wherever one is required. The compiler is, obviously, wrong:

```tut:book:fail
s.toLowerCase
```

Whenever you're using `null`, you're hindering the compiler's ability to prove your code correct (or not).

## It's not pleasant

The existence of `null` is terribly unpleasant: it means that you can't trust any value to not be `null`. So, in theory, you'd be forced to write code like:

```tut:silent
def concat(a: String, b: String): String = {
  if(a == null)      b
  else if(b == null) a
  else               s"$a$b"
}
```

That's a lot of boilerplate. In Scala, the general convention is that we pretend `null` does not exist and any value not wrapped in an [`Option`] cannot be `null`. This allows us to rewrite the above function:

```tut:silent
def concat(a: String, b: String): String = s"$a$b"
```

Just as importantly, if we were to write `concat` such that either `a` or `b` might not be set, the compiler forces us to deal with this:

```tut:silent
def concat(a: Option[String], b: Option[String]): String = s"${a.getOrElse("")}${b.getOrElse("")}"
```

# Exception to the rule

There exists at least one scenario in which you must use `null`: Java interop. Java being what it is, some of its APIs use `null` to mean _no value there_, and you don't have much choice but to comply.

The standard [`URI`] class, for example, expect `null` values in its constructor - if you don't have a _fragment_, say, stick `null` instead and [`URI`] will work it out.

[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
[`URI`]:https://docs.oracle.com/javase/8/docs/api/java/net/URI.html
