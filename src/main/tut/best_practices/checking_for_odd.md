---
title: Compare remainder to 0 when checking for oddness.
layout: article
section: best_practice
category: numbers
---

> When checking whether a number is odd, always use `x % 2 != 0` rather than the more common `x % 2 == 1`.

# Reason

It might not seem like a big difference, but there are odd numbers whose division by 2 has a remainder that's not 1:

```tut:book
-3 % 2
```

The mathematical definition of odd and even numbers is:
* an even number is one whose division by 2 has no remainder
* an odd number is not even

This suggests the following implementations:

```tut:silent
def isEven(i: Int): Boolean = i % 2 == 0

def isOdd(i: Int): Boolean = i % 2 != 0
```

And, indeed:

```tut:book
-3 % 2 != 0
```
