---
title: Error ADTs should extend Exception
layout: article
section: best_practice
category: data_types
---

> When creating an [ADT] to represent errors, have the base type extend [`Exception`].

# Reason

Some APIs, such as the standard [`Try`] and [`Future`], represent errors through exceptions. If your error type doesn't extend [`Exception`], you cannot put stick it in a [`Failure`], for example.

Let's say we have this simple [ADT] for database-related errors:

```tut:silent
sealed abstract class DbError extends Product with Serializable

object DbError {
  final case object InvalidSql extends DbError
  final case object ConnectionLost extends DbError
}
```

We also must implement an abstract method will the following signature:

```scala
def foo(i: Int): Try[Int]
```

With our current `DbError` implementation, it's impossible to write something like:

```tut:book:fail
def foo(i: Int) = scala.util.Failure(DbError.InvalidSql)
```

Had `DbError` extended [`Exception`] however, this would have been perfectly possible.

# Secondary reason

There's another reason for this rule, although I find it dubious.

Imagine you have two different error [ADTs][ADT] - our `DbError` one, and another that's more about business logic:

```tut:silent
sealed abstract class UserError extends Exception with Product with Serializable

object UserError {
  final case object NotFound extends UserError
  final case object Unauthorized extends UserError
}
```

If you try to write a function that attempts to find a user in database and validate its password, its error type is either a `DbError` or a `UserError`. There are ways to represent that - [`Either`] (but that gets messy very quickly if we have more than 2 error types) or a unifying [ADT] that wraps both types, for instance.

If both `DbError` and `UserError` had extended [`Exception`], we'd have a third option: [`Exception`] being a sort of universal supertype for errors, we could just say that our error type is [`Exception`] and the compiler would be perfectly happy.

I personally dislike this approach because it removes the compiler's ability to check whether you've dealt with all error cases. You must manually chose which subtypes of [`Exception`] you want to deal with, and the compiler has no way of knowing if you've dealt with all error types for a given call, or if the types you deal with are actually possible in a given context.

[`Exception`]:https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Exception.html
[`Try`]:https://www.scala-lang.org/api/2.12.8/scala/util/Try.html
[`Future`]:https://www.scala-lang.org/api/2.12.8/scala/concurrent/Future.html
[`Failure`]:https://www.scala-lang.org/api/2.12.8/scala/util/Failure.html
[`Either`]:https://www.scala-lang.org/api/2.12.8/scala/util/Either.html
[ADT]:../definitions/adt.html
