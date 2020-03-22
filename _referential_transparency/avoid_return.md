---
title: Do not use return
layout: article
linters:
  - name: wartremover
    rules:
      - name: Return
        url:  http://www.wartremover.org/doc/warts.html#return
  - name: scapegoat
    rules:
      - name: UnnecessaryReturnUse
  - name: scalastyle
    rules:
      - name: ReturnChecker
        url:  http://www.scalastyle.org/rules-1.0.0.html#org_scalastyle_scalariform_ReturnChecker
---

> There's never a good reason to use the `return` keyword.

# Reasons

## It makes code hard to reason about

There's an argument to be made that the more ways there are to reach a given line of code, the harder that code is to reason about.

See [GOTO statements considered harmful] for an eloquent dissertation on the matter.

## It's referentially opaque

If `return` were [referentially transparent][reftrans], you should be able to inline expressions that use it and not change the meaning of a program.

Here's `foo1`, a method whose return values are pre-computed and stored in values:

```scala
def foo1(i: Int): Int = {
  val pos = return i
  val neg = return -i

  if(i > 0) pos
  else      neg
}
```

And `foo2`, the same method but with the return values inlined:

```scala
def foo2(i: Int): Int = {
  if(i > 0) return i
  else      return -i
}
```

If `return` was [referentially transparent][reftrans], we'd get the same `foo1` and `foo2` output for the same input, but:

```scala
foo1(-1)
// res0: Int = -1
foo2(-1)
// res1: Int = 1
```

## You (probably) don't understand what it does

Let's take the following, fairly straightforward method:

```scala
def foo(is: List[Int]): List[Int] = is.map(n => return n + 1)
// error: type mismatch;
//  found   : Int
//  required: List[Int]
// def foo(is: List[Int]): List[Int] = is.map(n => return n + 1)
//                                                        ^^^^^
```

The compilation error doesn't seem to make much sense - how can a [`map`] on a [`List[Int]`][`List`] yield an [`Int`]? But the compiler tends to be smarter than us about these things, so let's play along.

```scala
def foo(is: List[Int]): Int = is.map(n => return n + 1)
// error: type mismatch;
//  found   : List[Nothing]
//  required: Int
// def foo(is: List[Int]): List[Int] = is.map(n => return n + 1)
//                                           ^
```

It's unclear how [`Nothing`] got mixed up in there, and is usually the sign that something went south, but fine, down the rabbit hole we go:


```scala
def foo(is: List[Int]): List[Nothing] = is.map(n => return n + 1)
// error: type mismatch;
//  found   : Int
//  required: List[Nothing]
// def foo(is: List[Int]): List[Int] = is.map(n => return n + 1)
//                                                        ^^^^^
```

`(╯°□°）╯︵ ┻━┻`

I don't understand what's going on there, nor do I really want to. `return` is mad. Don't use it.

[`List`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html
[`map`]:https://www.scala-lang.org/api/2.12.8/scala/collection/immutable/List.html#max:A
[GOTO statements considered harmful]:https://homepages.cwi.nl/~storm/teaching/reader/Dijkstra68.pdf
[`Nothing`]:https://www.scala-lang.org/api/2.12.8/scala/Nothing.html
[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
[reftrans]:../definitions/referential_transparency.html

