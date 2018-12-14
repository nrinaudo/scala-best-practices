---
title: Binary compatibility
layout: article
---

Two versions of a library are said to be binary compatible if you can swap one out for the other without recompiling.

Let's imagine a `Foo` library such that its `1.0.0` and `1.1.0` versions are binary incompatible (even though they are source compatible).
If you have three projects such that:
- `A` depends on `Foo 1.0.0`
- `B` depends on `Foo 1.1.0`
- `C` depends on `A` and `B`

Whoever maintains `C` will find himself dealing with some very weird runtime errors that might be a bit a nightmare to work out - that person might not even know that `Foo` exists, let alone that it's in `C`'s dependencies.

It's best to strive for binary compatibility for this reason. Tools such as [MiMa](https://github.com/lightbend/migration-manager) have been created to help library author with this task.
