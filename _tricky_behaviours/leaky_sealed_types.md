---
title: Make subtypes of sealed types final
layout: article
linters:
  - name: wartremover
    rules:
      - name: LeakingSealed
        url:  http://www.wartremover.org/doc/warts.html#leakingsealed
---

> When writing a subtype for a [`sealed`] type, mark it as [`final`].

# Reason

Most people will instinctively assume that a [`sealed`] type cannot have implementations in another source file - that's what a type being [`sealed`] means, right?

Well, not quite. A [`sealed`] type can only have *direct* subtypes in the same source file. But the constraint is not transitive and these subtypes, not being [`sealed`] themselves, can have subtypes declared in other files.

For example, having this in one file:

```scala
sealed trait Foo

class Bar extends Foo
```

We can absolutely extend `Bar` in another source file:

```scala
class FooBar extends Bar
```

This is perfectly correct as per the definition of [`sealed`], and sometimes even desirable. It's just not what most people expect.

For this reason, it's preferable to default to [`final`] subtypes unless you have a good reason for them not to be.

[`sealed`]:../definitions/sealed.html
[`final`]:../definitions/final.html

