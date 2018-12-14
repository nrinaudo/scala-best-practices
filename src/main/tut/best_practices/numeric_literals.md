---
title: Use upper case letters with literal syntax for numbers
layout: article
section: best_practice
category: numbers
---

> When declaring literal numbers such as [`Long`] or [`Float`], use upper case letter. For example, prefer `2L` to `2l`.

# Reason

Depending on the font and the syntax highlighting scheme, some letters look a lot like numbers. In some configurations, for example, `l` and `1` are basically the same. Github, for instance, while not the worst offender, isn't doing a great job there.

To the casual reader, the following has a fair chance of looking like a list of [`Int`]s, when a [`Long`] sneaked in:

```tut:book
List(1, 11, 111, 111l, 11111, 11111, 11111)
```

Using upper-case letters makes this a lot more obvious:

```tut:book
List(1, 11, 111, 111L, 11111, 11111, 11111)
```

[`Long`]:https://www.scala-lang.org/api/2.12.8/scala/Long.html
[`Float`]:https://www.scala-lang.org/api/2.12.8/scala/Float.html
[`Int`]:https://www.scala-lang.org/api/2.12.8/scala/Int.html
