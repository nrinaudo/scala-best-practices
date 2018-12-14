enablePlugins(TutPlugin, GhpagesPlugin)

scalaVersion                  := "2.12.8"
tutTargetDirectory            := siteDirectory.value
includeFilter in makeSite     := "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
makeSite                      := makeSite.dependsOn(tut).value
git.remoteRepo                := "git@github.com:nrinaudo/scala-best-practices.git"
ghpagesNoJekyll               := false
