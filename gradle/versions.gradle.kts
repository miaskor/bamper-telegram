def artifactsMap = concatMaps (
    version(
      "3.15.1", [
        "org.jooq:jooq-meta",
        "org.jooq:jooq-codegen"
      ]
    )
    )

static def concatMaps(Map... maps) {
  maps.collectEntries()
}

static def version(String version, List<String> artifacts) {
  def map =[:]
  artifacts.each { map.put(it, version) }
  map
}

ext.lib = {
  String artifact ->
  artifact + ':' + artifactsMap[artifact]
}

/*
* fun lib(artifact: String): String {
  return artifactMap[artifact]
}


private fun concatMaps(vararg maps: Map<String, String>) {
  val map = mapOf<String, String>()
  maps.forEach {
    map.plus(it)
  }
}

private fun version(version: String, artifacts: List<String>): Map<String, String> {
  return artifacts.map { it.to(version) }
}

private val artifactMap = concatMaps(
  version(
    "3.15.1", listOf(
      "org.jooq:jooq-meta",
      "org.jooq:jooq-codegen"
    )
  )
)
* */
