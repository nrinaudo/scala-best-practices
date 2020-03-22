---
title: Add explicit type annotations to implicits
layout: article
linters:
  - name: wartremover
    rules:
      - name: ExplicitImplicitTypes
        url:  http://www.wartremover.org/doc/warts.html#explicitimplicittypes
---

> Always add an explicit type to your implicit values or methods, even when they're private

# Reason

Aside from [binary compatibility concerns](../binary_compat/explicit_type_annotations.html), implicit resolution is complex and, sometimes, buggy.

The following code compiles:

```scala
trait Foo[_]
class Bar

implicitly[Foo[Bar]]

object Bar {
  implicit val impFoo: Foo[Bar] = new Foo[Bar] {}
}
```

`impFoo` is in the [implicit scope](../definitions/implicit_scope.html) of `Foo[Bar]`, and our `implicitly[Foo[Bar]]` resolves.

But, because of [bug 8697](https://github.com/scala/bug/issues/8697), the following does not:

```scala
trait Foo[_]
class Bar

implicitly[Foo[Bar]]

object Bar {
  implicit val impFoo = new Foo[Bar] {}
}
// error: could not find implicit value for parameter e: repl.Session.App0.Foo[repl.Session.App0.Bar]
// implicitly[Foo[Bar]]
// ^^^^^^^^^^^^^^^^^^^^
```

The only difference between the two is that `impFoo` has an explicit type in the first snippet but not in the second.

The behaviour is clearly counter-intuitive, and considered to be a bug - but it results in a compilation failure that's trivial to fix once you know the problem.

Well. Most of the time. There are [cases](https://github.com/rickynils/scalacheck/pull/468) where it will result in the *wrong* implicit value being picked, which is a lot harder to realise and fix.
