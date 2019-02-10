---
title: Compare remainder to 0 when checking for oddness
layout: article
linters:
  - name: scapegoat
    rules:
      - name: BrokenOddness
---

> When checking whether a number is odd, always use `x % 2 != 0` rather than the more common `x % 2 == 1`.

Note that, unlike most rules here, this one is not Scala specific.

# Reason

There are odd numbers whose division by 2 has a remainder that's not 1:

```scala
-3 % 2
// res0: Int = -1
```

The [mathematical definition](https://en.wikipedia.org/wiki/Parity_(mathematics)) of odd and even numbers is:
* an even number is one whose division by 2 has no remainder
* an odd number is not even

This suggests the following implementations:

```scala
def isEven(i: Int): Boolean = i % 2 == 0

def isOdd(i: Int): Boolean = i % 2 != 0
```

And, indeed:

```scala
-3 % 2 != 0
// res1: Boolean = true
```

