---
title: Avoid unicode versions of ASCII operators
layout: article
linters:
  - name: scalastyle
    rules:
      - name: NonASCIICharacterChecker
        url:  http://www.scalastyle.org/rules-1.0.0.html#org_scalastyle_scalariform_NonASCIICharacterChecker
  - name: scalafmt
    rules:
      - name: rewriteTokens
        url:  https://scalameta.org/scalafmt/docs/configuration.html#rewritetokens
---

> ASCII operators (such as `->`) should be preferred over their fancy unicode equivalents (such as `→`).

# Reasons

## Not equivalent to their ASCII counterparts

While they're supposed to be equivalent, they're not, at least as far as [priority](https://www.scala-lang.org/files/archive/spec/2.12/06-expressions.html#infix-operations) is concerned:
* `->` has a lower priority than `/`
* `→` has a higher priority than `/`

This makes the following code legal:

```scala mdoc
1 -> 4 / 2 // Equivalent to 1 -> (4 / 2)
```

The following code, on the other hand, isn't:

```scala mdoc:fail
1 → 4 / 2 // Equivalent to (1 → 4) / 2
```

This violates reasonable assumptions about the behaviour of unicode operators, and is an accident waiting to happen. Better to avoid them altogether and rely on [fonts with ligature support](https://github.com/tonsky/FiraCode) or [editor support](https://emacsredux.com/blog/2014/08/25/a-peek-at-emacs-24-dot-4-prettify-symbols-mode/) for fancy display.


## Soon to be deprecated

As it happens, the feature is controversial enough that it's going to be [deprecated](https://github.com/scala/scala/pull/7540) in an upcoming Scala version.
