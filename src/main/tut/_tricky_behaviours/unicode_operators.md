---
title: Avoid unicode versions of ASCII operators
layout: article
---

> ASCII operators (such as `->`) should be preferred over their fancy unicode equivalents (such as `→`).

# Reason

While they're supposed to be equivalent, they're not, at least as far as [priority](https://www.scala-lang.org/files/archive/spec/2.12/06-expressions.html#infix-operations) is concerned:
* `->` has a lower priority than `/`
* `→` has a higher priority than `/`

This makes the following code is legal:

```tut:book
1 -> 4 / 2 // Equivalent to 1 -> (4 / 2)
```

The following code, on the other hand, isn't:

```tut:book:fail
1 → 4 / 2 // Equivalent to (1 → 4) / 2
```

This violates reasonable assumptions about the behaviour of unicode operators, and is an accident waiting to happen. Better to avoid them altogether and rely on [fonts with ligature support](https://github.com/tonsky/FiraCode) or [editor support](https://emacsredux.com/blog/2014/08/25/a-peek-at-emacs-24-dot-4-prettify-symbols-mode/) for fancy display.
