enablePlugins(MdocSitePlugin, GhpagesPlugin)

scalaVersion              :=  "2.12.8"
scalacOptions             ++= Seq("-feature", "-language:implicitConversions", "-language:reflectiveCalls")
mdocIn                    :=  (sourceDirectory in Compile).value / "mdoc"
mdocExtraArguments        +=  "--no-link-hygiene"
includeFilter in makeSite :=  "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
git.remoteRepo            :=  "git@github.com:nrinaudo/scala-best-practices.git"
ghpagesNoJekyll           :=  false
