---
title: Add explicit type annotations to public members
layout: article
linters:
  - name: wartremover
    rules:
      - name: PublicInference
        url:  http://www.wartremover.org/doc/warts.html#publicinference
---

> Always add an explicit type to your public members, even when you're happy with what's being inferred.

# Reason

The compiler relies on implementation details to infer types, and implementation details can change - which can in turn break [binary compatibility][bincompat].

Type inference tries to work out the most specific subtype of all types a value could be. Take the following code, for example:

```scala
def foo(i: Int) = {
  if(i % 2 == 0) Some(i)
  else           None
}

foo(1)
// res0: Option[Int] = None
```

[`Some[Int]`][`Some`] and [`None`] share a common direct supertype: [`Option[Int]`][`Option`] (well, [not quite](../adts/product_with_serializable.html), but close enough for our purposes). This allows the compiler to correctly infer `foo`'s return type, but what if we were to change the implementation in a later version?

```scala
def foo(i: Int) = Some(i)

foo(1)
// res2: Some[Int] = Some(1)
```

The return type is no longer [`Option[Int]`][`Option`] but [`Some[Int]`][`Some`], and just like that, we've broken [binary compatibility][bincompat].

Explicit type annotations on public members ensure that implementation details don't leak out and that we don't accidentally break things without meaning to, or even realizing.

# Mistaken assumption: implementing abstract members

A common assumption that turns out to be incorrect is that implementing abstract members (or overriding concrete ones) is fine, since the parent type will be inferred.

This turns out to be incorrect:

```scala
abstract class Foo {
  def getOpt[A](a: A): Option[A]
}

class FooImpl extends Foo {
  override def getOpt[A](a: A) = Some(a)
}
```

It's usually assumed that `FooImpl.getOpt` will return the right type, but this is wrong:

```scala
new FooImpl().getOpt(1)
// res3: Some[Int] = Some(1)
```

Changing `FooImpl.getOpt` implementation to return either a [`Some[Int]`][`Some`] or an [`Option[Int]`][`Option`] will break [binary compatibility][bincompat] just as much as the general case.


[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
[`Some`]:https://www.scala-lang.org/api/2.12.8/scala/Some.html
[`Any`]:https://www.scala-lang.org/api/2.12.8/scala/Any.html
[bincompat]:../definitions/binary_compatibility.html
[`None`]:https://www.scala-lang.org/api/2.12.8/scala/None$.html

