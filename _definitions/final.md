---
title: Final types
layout: article
---

Marking a type as `final` means that it can never have subtypes.

For example, given:

```scala
final class Foo
```

Then the following can't compile:

```scala
class Bar extends Foo
// <console>:13: error: illegal inheritance from final class Foo
//        class Bar extends Foo
//                          ^
```
