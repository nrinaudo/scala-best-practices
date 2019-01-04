---
title: Do not throw exceptions
layout: article
linters:
  - name: wartremover
    rules:
      - name: Throw
        url:  http://www.wartremover.org/doc/warts.html#throw
---

> Do not throw exceptions if you can possibly avoid it.

In particular, when:
* something might not be there, use [`Option`].
* something might fail, use [`Either`].
* something is rude and might throw an [`Exception`], use [`Try`].

# Reasons

## They're referentially opaque

Throwing an [`Exception`] breaks [referential transparency][reftrans].

This can be demonstrated fairly easily. If `throw` was [referentially transparent][reftrans], by definition, the two following methods would be equivalent:

```tut:silent
def foo1() = if(false) throw new Exception else 2

def foo2() = {
  val a = throw new Exception
  if (false) a  else 2
}
```

Turns out, however, that they aren't.

`foo1` terminates:

```tut:book
foo1()
```

`foo2` fails with an exception:

```tut:book:fail
foo2()
```

## They're unsafe

Scala uses unchecked exceptions, which means that the compiler is not aware of them, and cannot check whether they're dealt with properly. A function that throws is a bit of a lie: its type implies it's [total function](../definitions/total_function.html) when it's not.

Let's take a trivial example:

```tut:silent
def foo(i: Int) = throw new Exception
```

As far as the type checker is concerned, this function is perfectly fine and it'll happily accept the following:

```tut:book:fail
foo(1)
```

This blows up at runtime, which is really something we'd like to avoid.

# Exceptions to the rule

It's perfectly fine to throw an exception for truly exceptional errors. CPU not found? Critical hard-drive failure? a required resource hasn't been bundled with the binaries? throw away.

But scenarios such as _parse this string into an int_ should never throw - a [`String`] not being an [`Int`] is not exceptional, it's the normal case! There are many more [`String`]s that aren't valid [`Int`]s than the converse.

[`Exception`]:https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html
[reftrans]:../definitions/referential_transparency.html
[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
[`Try`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html
[`Either`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either.html
