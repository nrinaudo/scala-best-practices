---
title: Avoid mutability
layout: article
linters:
  - name: wartremover
    rules:
      - name: Var
        url:  http://www.wartremover.org/doc/warts.html#var
      - name: MutableDataStructures
        url: http://www.wartremover.org/doc/warts.html#mutabledatastructures
---

> Mutability is almost always a bad idea and should be avoided.

# Reasons

## It's referentially opaque

Mutability very often breaks [referential transparency][reftrans].

Let's take the following method:

```scala mdoc:silent
var i: Int = 0

def foo(j: Int): Int = {
  i += j
  i
}
```

If it were [referentially transparent][reftrans], it'd always yield the same output for the same input. This is clearly not true:

```scala mdoc
foo(1)

foo(1)
```


## It's often premature optimisation

Mutability is very often (but not always) used for performance reasons - it's unfortunately been drilled into generations of programmers that instance reuse is the key to fast code.

There are a few problems with that assertion, though. First, it's just not really true - immutable data structures, for example, often have the same asymptotic complexity as their mutable counterparts, and are much, much simpler to both implement and work with.

Whether or not you believe this to be true, however, is irrelevant, provided you agree that you use mutability to write faster code. If you agree with that, then you agree that mutability is an optimisation - it means the same thing. And optimisations, especially ones that come at such a high cost ([referential opacity][reftrans]), should only be applied when they actually matter, not by default. Optimising by default is called _premature optimisation_ and it's not a good thing.

Whenever you feel like using mutability, ask yourself if it's actually worth it. Run benchmarks! If mutability gets you an incredible 50% speedup on a bit of code that's 1% of your runtime, that's a 0.5% speedup overall. Is that really worth the maintenance / complexity cost?

# Exceptions to the rule

Mutability is acceptable when it doesn't break [referential transparency][reftrans]. The most typical example of that is local mutability:

```scala mdoc
def sum(is: List[Int]): Int = {
  var s = 0
  for(i <- is) s += i
  s
}
```

The fact that `s` is mutable isn't exposed to the rest of the world and `sum` is [referentially transparent][reftrans]. If using mutability this way solves some performance bottleneck, go for it!

Just for the record though, this example is much cleaner without mutability:

```scala mdoc:reset
def sum(is: List[Int]): Int = is.fold(0)(_ + _)
```


[reftrans]:../definitions/referential_transparency.html
