enablePlugins(TutPlugin, GhpagesPlugin)

scalaVersion                  := "2.12.8"
includeFilter in makeSite     := "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
git.remoteRepo                := "git@github.com:nrinaudo/scala-best-practices.git"
ghpagesNoJekyll               := false

val tutDirName = settingKey[String]("tut output directory")
tutDirName := "./"
addMappingsToSiteDir(tut, tutDirName)
