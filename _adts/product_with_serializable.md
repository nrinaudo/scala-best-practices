---
title: Make ADTs subtypes of Product and Serializable
layout: article
---

> When writing an [ADT], always have the root type extend [`Product`] and [`Serializable`].

# Reason

It allows the compiler to infer better types than it would otherwise.

Given the following simple [ADT]:

```scala
sealed abstract class Status
object Status {
  final case object Ok extends Status
  final case object Nok extends Status
}
```

The compiler will sometimes infer some wonky types:

```scala
List(Status.Ok, Status.Nok)
// res0: List[Product with Serializable with Status] = List(Ok, Nok)
```

This happens because inferring types is done by looking at the most specific supertype of `Status.Ok` and `Status.Nok`.

`Status` *is* a supertype of both, but it's not the most specific one: `Status.Ok` and `Status.Nok` both also extend [`Product`] and [`Serializable`] by virtue of being case objects.

As far as the compiler is concerned, the most precise supertype it can find for both `Status.Ok` and `Status.Nok` is thus `Product with Serializable with Status`.

Now, consider the following implementation - the only difference is in the types that `Status` extend:

```scala
sealed abstract class Status extends Product with Serializable
object Status {
  final case object Ok extends Status
  final case object Nok extends Status
}
```

Here, the most specific supertype of both `Status.Ok` and `Status.Nok` is `Status`, which leads to the correct types being inferred:

```scala
List(Status.Ok, Status.Nok)
// res1: List[Status] = List(Ok, Nok)
```

[ADT]:../definitions/adt.html
[`Product`]:https://www.scala-lang.org/api/2.12.8/scala/Product.html
[`Serializable`]:https://www.scala-lang.org/api/2.12.8/scala/Serializable.html
