---
title: Avoid using unicode equivalents to ASCII operators
layout: article
section: best_practice
category: safety
---

> However nice they look, do not use fancy unicode equivalents to ASCII operators, such as `→` for `->`.

# Reason

While they're supposed to be equivalent, they're not, at least as far as [priority](https://www.scala-lang.org/files/archive/spec/2.12/06-expressions.html#infix-operations) is concerned:
* `->` has a lower priority than `/`
* `→` has a higher priority than `/`

This means that the following code is legal:

```tut:book
// Equivalent to 1 -> (4 / 2)
1 -> 4 / 2
```

While the following isn't:

```tut:book:fail
// Equivalent to (1 → 4) / 2
1 → 4 / 2
```

This violates reasonable assumptions about the behaviour of unicode operators, and is an accident waiting to happen. Better to avoid them altogether and rely on fonts with ligature support or [editor support](https://emacsredux.com/blog/2014/08/25/a-peek-at-emacs-24-dot-4-prettify-symbols-mode/) for fancy display.
