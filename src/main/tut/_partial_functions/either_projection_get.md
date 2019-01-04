---
title: Do not call get on an Either projection
layout: article
linters:
  - name: wartremover
    rules:
      - name: EitherProjectionPartial
        url:  http://www.wartremover.org/doc/warts.html#eitherprojectionpartial
  - name: scapegoat
    rules:
      - name: EitherGet
---

> When retrieving the content of an [`Either`], do not use [`get`] on one of its projections.

# Reason

Some projections are [left projections][`LeftProjection`] of a [`Right`], or [right projections][`RightProjection`] of a [`Left`], and [`get`] deals with them by throwing an exception:

```tut:book:fail
Left(1).right.get

Right(1).left.get
```

If you have a default value to provide for the "other" case, use [`getOrElse`] on a projection:

```tut:book
Left(1).right.getOrElse(-1)
```

Alternatively, if you're using the common convention of treating the [`Left`] side of an [`Either`] as the error case, you can also call [`getOrElse`][biasedGetOrElse] directly on the [`Either`]:

```tut:book
Left(1).getOrElse(-1)
```

Another practical approach is to use [`fold`], which lets you provide a handler for each case:

```tut:book
(Left(1): Either[Int, Boolean]).fold(
  i => s"Found an int: '$i'",
  b => s"Found a boolean: '$b'"
)
```

[`Either`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either.html
[`Right`]:https://www.scala-lang.org/api/2.12.8/scala/util/Right.html
[`Left`]:https://www.scala-lang.org/api/2.12.8/scala/util/Left.html
[`RightProjection`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either$$RightProjection.html
[`LeftProjection`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either$$LeftProjection.html
[`fold`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either.html#fold[C](fa:A=%3EC,fb:B=%3EC):C
[`getOrElse`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either$$RightProjection.html#getOrElse[B1%3E:B](or:=%3EB1):B1
[`get`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html#get:T
[biasedGetOrElse]:https://www.scala-lang.org/api/2.12.8/scala/util/Either.html#getOrElse[B1%3E:B](or:=%3EB1):B1
