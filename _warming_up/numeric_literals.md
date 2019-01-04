---
title: Use upper case numeric literal suffixes
layout: article
linters:
  - name: scalastyle
    rules:
      - name: UppercaseLChecker
        url:  http://www.scalastyle.org/rules-1.0.0.html#org_scalastyle_scalariform_UppercaseLChecker
---

> When declaring literal numbers such as [`Long`] or [`Float`], use upper case suffix. For example, prefer `2L` to `2l`.

# Reason

Depending on the font and the syntax highlighting scheme, some letters look a lot like numbers. In some configurations, for example, `l` (lower case `L`) and `1` (one) are basically indistinguishable. Github, for instance, while not the worst offender, isn't doing a great job there.

To the casual reader, the following has a fair chance of looking like a list of [`Int`]s, but a [`Long`] sneaked in:

```scala
List(1, 11, 111, 111l, 11111, 11111, 11111)
// res0: List[Long] = List(1, 11, 111, 111, 11111, 11111, 11111)
```

Using upper-case letters makes this a lot more obvious:

```scala
List(1, 11, 111, 111L, 11111, 11111, 11111)
// res1: List[Long] = List(1, 11, 111, 111, 11111, 11111, 11111)
```

[`Long`]:https://www.scala-lang.org/api/2.12.8/scala/Long.html
[`Float`]:https://www.scala-lang.org/api/2.12.8/scala/Float.html
[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
