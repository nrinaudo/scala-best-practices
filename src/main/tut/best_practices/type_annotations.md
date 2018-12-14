---
title: Public members should have explicit type annotations
layout: article
section: best_practice
category: safety
---

> Always add an explicit type to your public members, even when you're happy with what's being inferred.

# Reason

Type inference tries to work out the most specific subtype of all types a value could be - the _most specific_ part is important, because otherwise we'd always get the universal supertype, [`Any`].

Sometimes, this is non ambiguous - if the set of all possible types is a singleton:

```tut:book
// All paths yield an Int, so there's no ambiguity here.
def foo(i: Int) = {
  if(i % 2 == 0) i
  else           i - 1
}
```

Other times, it works [more or less](./adt_type_inference.html) as you'd want it to:

```tut:book
def foo(i: Int) = {
  if(i % 2 == 0) Some(i)
  else           None
}
```

The problem with this is that `foo`'s return type is entirely dependent on its implementation. This seems obvious, but what if that implementation were to change at a later date?

```tut:book
def foo(i: Int) = Some(i)
```

The return type is no longer [`Option[Int]`][`Option`] but [`Some[Int]`][`Some`], and just like that, we've broken binary compatibility.

Explicit type annotations on public members ensure that implementation details don't leak out and that we don't accidentally break things without meaning to.

# Exception to the rule

It's ok not to add a type annotation when implementing abstract members or overriding concrete ones: this will default to the parent's type, which is the right behaviour.


[`Option`]:https://www.scala-lang.org/api/2.12.8/scala/Option.html
[`Some`]:https://www.scala-lang.org/api/2.12.8/scala/Some.html
[`Any`]:https://www.scala-lang.org/api/2.12.8/scala/Any.html
