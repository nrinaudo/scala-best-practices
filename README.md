# Building the site

## Required tools

The following tools are needed to build the site:
* [sbt], to compile and assemble all examples
* [jekyll], to turn the output into a static site

## Steps

You first need to run all pages through the amazing [mdoc](https://github.com/scalameta/mdoc) to make sure all examples are valid:

```shell
sbt makeSite
```

This will generate [jekyll] sources in `./target/site`. For local development / browsing, the simplest thing to do is to run the following from `./target/site`:

```shell
jekyll serve --watch -c _config.yml --baseurl ""
```

This will make the entire site available on `http://localhost:4000`.

[jekyll]:https://jekyllrb.com/
[sbt]:https://www.scala-sbt.org/
