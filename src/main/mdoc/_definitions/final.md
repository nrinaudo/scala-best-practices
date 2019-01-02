---
title: Final types
layout: definition
---

Marking a type as `final` means that it can never have subtypes.

For example, given:

```scala mdoc
final class Foo
```

Then the following can't compile:

```scala mdoc:fail
class Bar extends Foo
```
