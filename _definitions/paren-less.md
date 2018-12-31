---
title: Paren-less methods
layout: article
---

A paren-less method is a method that doesn't take parameters (a nullary method) and that is declared without parentheses:

```scala
def foo: Int = 1
```

Trying to call is with parentheses will fail:

```scala
foo()
// <console>:14: error: Int does not take parameters
//        foo()
//           ^
```

An interesting property of paren-less methods is that they can be overridden (or implemented, if abstract) by `val`s.
