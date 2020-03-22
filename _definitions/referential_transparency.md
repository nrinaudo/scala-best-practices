---
title: Referential transparency
layout: definition
---

An expression is said to be referentially transparent if it can be replaced by its value and not change the program's behaviour.

For example, take the following method:

```scala
def add1(i: Int): Int = i + 1
```

It's referentially transparent because for any given `i`, you can use `add1(i)` or its result interchangeably.

This new version, however, isn't referentially transparent - we call such expressions _referentially opaque_:

```scala
def add1Bis(i: Int): Int = {
  println(i)
  i + 1
}
```

If it was referentially transparent, the two following methods would be strictly equivalent:

```scala
def foo1(i: Int): Int = {
  // We're using the result of add1Bis, twice.
  val a = add1Bis(i)

  a + a
}

def foo2(i: Int): Int =
  // We're calling add1Bis, twice.
  add1Bis(i) + add1Bis(i)
```

But they clearly are not:

```scala
foo1(1)
// 1
// res0: Int = 4

foo2(1)
// 1
// 1
// res1: Int = 4
```

# Why is it important?

This is more easily explained by showing what you lose without referentialy transparency.

```scala
var i: Int = 1

def addi(j: Int): Int = i + j
```

`addi` is clearly not referentially transparent: it doesn't necessarily return the same value for the same input.

Now, let's start 10 threads that'll increment `i`:

```scala
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

for(_ <- (1 to 10)) scala.concurrent.Future(i += 1)
```

What do you think `addi(1)` evaluates to now? Well, there's no way to know. It could be any number between 1 and 12, depending on how many of our threads have already completed. Let's try:

```scala
addi(1)
// res3: Int = 2
```

This uncertainty is something we must strive to avoid at all costs - how do you write correct code if you're not sure what happens when you run it?

*This* is what referential transparency gets us: certainty, and understanding of how our code works.


# Related terms

You'll often hear talk of purity and side effects. Those are just other terms for referentially transparency or opacity:
* a function is pure if referentially transparent
* a side effect is what makes a function impure

