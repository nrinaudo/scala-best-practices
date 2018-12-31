---
title: Final types
layout: article
---

Marking a type as `final` means that it can never have subtypes.

For example, given:

```tut:silent
final class Foo
```

Then the following can't compile:

```tut:book:fail
class Bar extends Foo
```
