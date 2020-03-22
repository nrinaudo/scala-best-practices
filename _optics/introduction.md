---
title: Introduction
layout: article
sequence: 0
---

Optics is a subject that I feel the Scala community is a bit weird about. Take the Haskell community, for example - they talk about optics *all the time*, and apparently even use them.

In the Scala community, however... not so much. We do have a fair amount of optics *implementations*, but I rarely hear them brought up or see them used in the wild.

One reason for this, I think, is how utterly terrifying the vocabulary is. Foci, lens, prism, affine traversal, profunctor optics... those are not friendly words. They're more like walls, telling you that you must be _this_ smart to use optics.

Turns out though, that if you disregard the theory, beautiful thought it may be (and it is beautiful), optics are a very useful tool, and a surprisingly easy to understand one. All you need to do is focus on the use cases.

The goal of this article is not to teach you everything there is to know about optics - that task is far beyond me. Rather, I would like to add them to that database of tools you keep at the back of your head so that one day, faced with a situation where they'd be useful, you remember. And if that happens, I would love to hear about it. I've gone through rather a lot of trouble to set this up and would be thrilled to hear it's not been for nothing.
