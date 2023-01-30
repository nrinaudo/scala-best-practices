---
title: Mark case classes as final
layout: article
linters:
  - name: wartremover
    rules:
      - name: FinalCaseClass
        url:  http://www.wartremover.org/doc/warts.html#finalcaseclass
  - name: scapegoat
    rules:
      - name: FinalModifierOnCaseClass
  - name: scalafix
    rules:
      - name: MissingFinal.noLeakingCaseClass = true
        url: https://github.com/vovapolu/scaluzzi
---

> When declaring a case class, make it [`final`].

# Reason

Extending a case class will produce behaviours that, while perfectly correct, can be surprising and certainly break reasonable expectations.

For example:

```scala mdoc
case class Foo(i: Int)
class Bar(i: Int, s: String) extends Foo(i)
```

`Bar` will behave oddly when it comes to comparison, hashing and printing:

```scala mdoc
new Bar(1, "foo") == new Bar(1, "bar")

Map(
  new Bar(1, "foo") -> "foo",
  new Bar(1, "bar") -> "bar"
)

new Bar(1, "baz")
```

This is largely due to Scala's reliance on Java constructs. In this instance:
* [`equals`], used to compare two values (and which maps to `==` in Scala)
* [`hashCode`], used to compute the hash of a value
* [`toString`], used to represent a value as a [`String`]

Case classes will generate correct implementations of these methods - [`equals`], for example, will run a field-for-field comparison.
The problem is that this breaks when you extend a case class: the subclass will inherit these methods, which are not aware of any field you might have added.

In the case of our `Bar` example, the `s` field is ignored by [`equals`], [`toString`] and [`hashCode`], which leads to the surprising (but perfectly correct) behaviour we observed.

# Exceptions to the rule

There's at least one scenario I know of where a non-[`final`] case class is desirable: when you wish to declare a case class but have absolute control over the values of its fields.

Let's say, for example, that you want to create a `PositiveInt` type wrapping an [`Int`] that is guaranteed to be positive. A naïve implementation would be:

```scala mdoc
final case class PositiveInt(value: Int)

object PositiveInt {
  def fromInt(i: Int): Option[PositiveInt] =
    if(i > 0) Some(new PositiveInt(i))
    else      None
}
```

The problem with this implementation is that you still have many ways to create values of type `PositiveInt` that containing a negative [`Int`]:

```scala mdoc
new PositiveInt(-1)

PositiveInt.fromInt(1).map(_.copy(-1))
```

In order to seal these holes, a common trick is to declare a `sealed abstract case class`:

```scala mdoc:reset
sealed abstract case class PositiveInt(value: Int)

object PositiveInt {
  def fromInt(i: Int): Option[PositiveInt] =
    if(i > 0) Some(new PositiveInt(i) {})
    else      None
}
```

Since `PositiveInt` is abstract, the compiler can't generate a constructor, a `copy` method or an `apply` method on the companion object and you effectively control the values that get inside it. But this requires it not to be [`final`].

So, as is often the case, there are scenarios in which this rule must be broken. It's a good default behaviour, to be changed when you must.

[`equals`]:https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
[`hashCode`]:https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--
[`toString`]:https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#toString--
[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
[`final`]:../definitions/final.html
[`String`]:https://docs.oracle.com/javase/8/docs/api/java/lang/String.html
