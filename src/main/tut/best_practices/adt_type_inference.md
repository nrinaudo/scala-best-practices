---
title: ADTs should be subtypes of Product and Serializable
layout: article
section: best_practice
category: data_types
---

> Whenever writing an [ADT], always have the root type extend [`Product`] and [`Serializable`].

# Reason

Given the following simple [ADT]:

```tut:silent
sealed abstract class Status
object Status {
  final case object Ok extends Status
  final case object Nok extends Status
}
```

The compiler will sometimes infer some wonky types:

```tut:book
List(Status.Ok, Status.Nok)
```

This happens because inferring types is done by looking at the most specific supertype of `Status.Ok` and `Status.Nok`.

`Status` *is* a supertype of both, but it's not the most specific one: `Status.Ok` and `Status.Ok` also both extend [`Product`] and [`Serializable`] by virtue of being case objects.

As far as the compiler is concerned, the most precise supertype it can find for both `Status.Ok` and `Status.Nok` is thus `Product with Serializable with Status`.

Now, consider the following implementation - the only difference is in the types that `Status` extend:

```tut:silent
sealed abstract class Status extends Product with Serializable
object Status {
  final case object Ok extends Status
  final case object Nok extends Status
}
```

Here, the most specific supertype of both `Status.Ok` and `Status.Nok` is `Status`, which leads to the correct types being inferred:

```tut:book
List(Status.Ok, Status.Nok)
```

# Exceptions to the rule

I don't know of any exception to this rule, provided all your subtypes are case classes or case objects.

The [`Product`] supertype makes it awkward to have anything else - you'd have to reimplement all the [`Product`] methods - but not impossible. And if you use a regular class, then the [`Serializable`] constraint can become a problem, especially with frameworks like Spark that love to use standard JVM serialisation.

[`Product`]:https://www.scala-lang.org/api/2.12.8/scala/Product.html
[`Serializable`]:https://www.scala-lang.org/api/2.12.8/scala/Serializable.html
[ADT]:../definitions/adt.html
