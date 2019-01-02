import com.typesafe.sbt.site.SitePlugin.autoImport._
import mdoc.MdocPlugin, MdocPlugin.autoImport._
import sbt._, Keys._
import sbt.plugins.{JvmPlugin, SbtPlugin}

/** Provides glue to integrate mdoc with sbt-site. */
object MdocSitePlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = JvmPlugin && MdocPlugin

  object autoImport {
    val mdocSite    = taskKey[Seq[(File, String)]]("create mdoc documentation in a way that lets sbt-site grab it")
    val mdocSiteOut = settingKey[String]("name of the directory in which sbt-site will store mdoc documentation")
  }

  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    mdocSite := {
      mdoc.toTask(" ").value
      val out = mdocOut.value
      for {
        (file, name) <- out ** AllPassFilter --- out pair Path.relativeTo(out)
      } yield file -> name
    },
    mdocSiteOut := "./",
    addMappingsToSiteDir(mdocSite, mdocSiteOut)
  )
}
