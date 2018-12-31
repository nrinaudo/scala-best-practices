---
title: Type class
layout: article
---

In the context of Scala, a type class is a family of types that support a known set of behaviours. This could be, for example, the family of types that expose a unique integer identifier.

This is implemented through a `trait` and any number of implicit instances of that trait.

Here's how our _has a unique id_ type class could be represented:

```scala
trait HasId[A] {
  def getId(a: A): Int
}
```

If you manage to get your hands on an instance of `HasId[A]` for a given `A`, you know how to retrieve its id - however this is actually implemented in `A`.

Providing instances is done through implicits:

```scala
final case class User(id: Int, name: String)

implicit val userHasId: HasId[User] = new HasId[User] {
  override def getId(user: User) = user.id
}
```

Instances are traditionally put in the companion object of either the type class or the type itself - here, either in `HasId` or `User`. Instances declared elsewhere (that  are not in the [implicit scope](./implicit_scope.html)) are said to be orphaned.

Instead of requiring a subtype of a known type, methods will require an implicit instance of the type class - a type and a proof that it supports the required behaviours.

For example:

```scala
def printId[A](a: A)(implicit hia: HasId[A]): Unit =
  println(s"Found id ${hia.getId(a)}")
```

`printId` isn't hard-coded to a single type, nor to a hierarchy of types with a known root, but to any type that can prove it supports the required behaviour - exposing an identifier.

This is a critical aspect of type classes: instances are linked to, but not baked into, types. You can provide instances later, or for types that you don't own.

Another important aspect is how type classes compose implicitly. Let's imagine the following:

```scala
trait HasLabel[A] {
  def getLabel(a: A): String
}
```

We could add immediate support for any type that has an `HasId` instance:

```scala
implicit def hasLabelFromId[A](implicit hia: HasId[A]): HasLabel[A] = new HasLabel[A] {
  override def getLabel(a: A) = s"ID:${hia.getId(a)}"
}
```

We can then rewrite our printing function:

```scala
def print[A](a: A)(implicit hla: HasLabel[A]): Unit =
  println(s"Label: ${hla.getLabel(a)}")
```

And to prove that this all works, let's call it on a `User`, for which we never explicitly provided an instance of `HasLabel`:

```scala
print(User(1, "Foo"))
// Label: ID:1
```

We've essentially just told the compiler that any type that exposed an identifier was also capable of exposing a label.
