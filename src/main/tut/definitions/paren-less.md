---
title: Paren-less methods
layout: article
---

A paren-less method is simply a method that doesn't take parameters (a nullary method) and that is declared without parentheses:

```tut:silent
def foo: Int = 1
```

Trying to call is with parentheses will fail:

```tut:fail:book
foo()
```
