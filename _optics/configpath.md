---
title: "Concrete use case: ConfigPath"
layout: article
sequence: 5
---

We've been focusing on optics as a way to navigate ADTs so far, but that was a bit of a simplification. In this last part, I'll show a possible use case (heavily inspired by circe's `JsonPath`) that doesn't actually use ADTs.

## Configuration ADT

A common way of representing configuration data is as a tree like structure:

```scala
sealed trait Config

case class Section(
  children: Map[String, Config]
) extends Config

case class Field(
  value: String
) extends Config
```

A `Config` is either a `Field` (a raw value, as a `String`) or a `Section`, which maps names to `Config` values. It's a recursive structure, since sections can contain other sections.

Here's an example of the kind of configuration we could store using that format:

```json
{
  "auth": {
    "token": "0xCAFEBABE"
  },
  "classifier": {
    "name"      : "news20",
    "classCount": "20"
  }
}
```

This maps directly to the following `Config` value:

```scala
val conf = Section(Map(
  "auth" -> Section(Map(
    "token" -> Field("0xCAFEBABE")
  )),
  "classifier" -> Section(Map(
    "name"       -> Field("news20"),
    "classCount" -> Field("20")
  ))
))
```

This is a convenient way of storing configuration, but accessing nested values can be awkward - you'd have to deal with the fact that:
* a key that you expect to be a `Section` might be a `Field`, or vice versa.
* a key that you expect to find doesn't exist.

This sounds a lot like the kind of problems the optics we've developed so far could alleviate. Note that `Map` isn't really an ADT though - as the presence and absence of keys is not known a compile-time; it's an entirely dynamic structure.

Let's see how far we get with the tools we've created.

## Configuration optics

First, the obvious prisms: splitting (diffracting, sorry, I've got to stay in theme) a `Config` in either a `Section` or a `Field`:

```scala
val section = Prism.fromPartial[Config, Section](
  setter = a => a,
  getter = { case a: Section => a }
)

val field = Prism.fromPartial[Config, Field](
  setter = a => a,
  getter = { case a: Field => a }
)
```

Then, we need some sort of way to explore sections. Given a key name, we want to be able to:
* retrieve the corresponding value, knowing it might not exist.
* set its associated value.

We wrote `Optional` to deal with this exact same scenario, so let's see if we can write an `Optional` to explore sections:

```scala
def sectionChild(name: String) = Optional[Section, Config](
  setter = (a, s) => Section(s.children + (name -> a)),
  getter = s      => s.children.get(name)
)
```

Note that it's a bit different than what we're used to. Since key names are not hard-coded in `Section`, we need to access them as parameters: we don't have a generic `Optional[Section, Config]`, but a way of creating one for a given key name.

## ConfigPath

Now that we have optics that allow us to explore the content of a `Config`, we can bundle them up in something that represents a path in our configuration tree:

```scala
case class ConfigPath(current: Optional[Config, Config]) {

  val asField   = composeOP(current, field)
  val asSection = composeOP(current, section)

  def child(name: String) = ConfigPath(
    composeOO(
      asSection,
      sectionChild(name)
    )
  )
}
```

`ConfigPath` contains an `Optional[Config, Config]` that contains the path we've explored so far.

`child` allows us to build a more complex path: it takes its `name` parameter, assumes the current path points to a `Section`, and attempts to go one level down.

`asField` and `asSection` allow us to transform whatever path we've built into a field or a section. `asField` is typically the last thing we'll call, as it'll yield an `Optiona[Config, Field]` which will allow us to work directly with the field at the end of our path.

The only problem we have with this data structure is: how do you create the first path - the one that points to the root of the configuration tree?

We need to be a little creative and write a sort of _identity_ `Optional`: given a `Config`, it will always return it:

```scala
val identityOpt = Optional[Config, Config](
  setter = (a, _) => a,
  getter = s      => Some(s)
)
```

This allows us to create the "root" `ConfigPath`, the path that points to the root of the configuration tree:

```scala
ConfigPath(identityOpt)
```

And this is how we'd use it:

```scala
val classifierName: Optional[Config, Field] =
  ConfigPath(identityOpt)
    .child("classifier")
    .child("name")
    .asField
```

We go from the root to `classifier` to `name`, and ask for that to be a field.

The purpose is clear enough, but the syntax not as pleasant as it could be. We can improve on that thanks to a bit of dark magic with Scala's `Dynamic`.

## Dynamic

`Dynamic` is a bit of an odd corner of Scala that gives us type safe syntax that looks a lot like dynamic code. The one we'll be focusing on is `selectDynamic`, which allows us to plug code when unknown members of a class are accessed.

Here's an example, where `UpCase` is a `Dynamic`:

```scala
import scala.language.dynamics

object UpCase extends Dynamic {
  def selectDynamic(missingMember: String): String =
    missingMember.toUpperCase
}
```

Any time a missing member is accessed, the compiler will transform that into a call to `selectDynamic` with the field's name as a parameter.

This allows us to write the following:

```scala
UpCase.bar
// res1: String = BAR
```

This is all type checked and verified, but it _does_ feel a little bit weird, doesn't it? The important thing is, `Dynamic` allows us to rewrite `ConfigPath` with a much nicer syntax.

## Dynamic ConfigPath

Knowing what we do now about `Dynamic`, we can rewrite `ConfigPath` to support `selectDynamic` instead of using `child`:

```scala
case class ConfigPath(
    current: Optional[Config, Config]
  ) extends Dynamic {

  val asField   = composeOP(current, field)
  val asSection = composeOP(current, section)

  def selectDynamic(child: String) = ConfigPath(
    composeOO(
      asSection,
      sectionChild(child)
    )
  )
}
```

This makes for much nicer syntax:

```scala
val classifierName =
  ConfigPath(identityOpt)
    .classifier
    .name
    .asField
```

This is *almost* good, but that `ConfigPath(identityOpt)` bit is clearly unpleasant. Let's stick that in a value:

```scala
val root = ConfigPath(identityOpt)
```

And we can now write configuration tree traversals with an extremely clear, readable syntax:

```scala
val classifierName=
  root
    .classifier
    .name
    .asField
```

Thanks to the work we've just done, we can now easily access or modify a nested configuration value:

```scala
classifierName.set(Field("NEWS20"))(conf)
// res2: Config = Section(Map(auth -> Section(Map(token -> Field(0xCAFEBABE))), classifier -> Section(Map(name -> Field(NEWS20), classCount -> Field(20)))))
```

## Key takeaways

We've learned a few neat things (`Dynamic`, for example), but the main point of this section was to show that you don't *need* ADTs for optics to be useful. As soon as you have immutable nested data that you need to explore, you might be able to simplify your code quite a bit with optics.
