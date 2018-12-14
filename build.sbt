enablePlugins(TutPlugin, PreprocessPlugin)

sourceDirectory in Preprocess := resourceManaged.value / "main" / "site-preprocess"
tutTargetDirectory            := (sourceDirectory in Preprocess).value
includeFilter in makeSite     := "*.yml" | "*.md" | "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.eot" | "*.svg" | "*.ttf" | "*.woff" | "*.woff2" | "*.otf"
makeSite                      := makeSite.dependsOn(tut).value
git.remoteRepo                := "git@github.com:nrinaudo/tabulate.git"
ghpagesNoJekyll               := false
