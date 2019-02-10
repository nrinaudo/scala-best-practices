---
title: Use isNaN when checking for NaN
layout: article
linters:
  - name: linter
    rules:
      - name: UseIsNanNotNanComparison
        url:  https://github.com/HairyFotr/linter/blob/master/src/test/scala/LinterPluginTest.scala#L1930
  - name: scapegoat
    rules:
      - name: NanComparison
---

> When checking whether a number is [`NaN`], use [`isNaN`] rather than `== NaN`.

Note that, unlike most rules here, this one is not Scala specific. `NaN` equality behaves unintuitively everywhere `NaN` exists.

# Reason

By [specification](https://en.wikipedia.org/wiki/IEEE_754), [`NaN`] is not equal to anything, not even itself:

```scala mdoc
Double.NaN == Double.NaN
```

Use [`isNaN`] instead, which is meant for just this task:

```scala mdoc
Double.NaN.isNaN
```

# Exception to the rule

There's one scenario in which you shouldn't use [`isNaN`]: if you're working with a lot of numbers and need to manually validate each one of them. [`isNaN`] can be a bit slow - it's been much improved in 2.12+, but still has a small cost.

If you identify this as a bottleneck, you always have the option of being clever. The two following methods produce the same result, but the second one is faster:

```scala mdoc
def slowFilterNaN(ds: Seq[Double]): Seq[Double] = ds.filter(d => !d.isNaN)

def fastFilterNaN(ds: Seq[Double]): Seq[Double] = ds.filter(d => d == d)
```

Note that this is an optimisation, and one that is sure to mystify some readers. It should only be used when a bottleneck is identified, as the runtime gain is otherwise guaranteed to be lower than the human cost of explaining it over and over again.

[`NaN`]:https://www.scala-lang.org/api/2.12.8/scala/Double$.html#NaN:Double(NaN)
[`isNaN`]:https://www.scala-lang.org/api/2.12.8/scala/Double.html#isNaN:Boolean
