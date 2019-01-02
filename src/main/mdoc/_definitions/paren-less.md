---
title: Paren-less methods
layout: definition
---

A paren-less method is a method that doesn't take parameters (a nullary method) and that is declared without parentheses:

```scala mdoc
def foo: Int = 1
```

Trying to call is with parentheses will fail:

```scala mdoc:fail
foo()
```

An interesting property of paren-less methods is that they can be overridden (or implemented, if abstract) by `val`s.
