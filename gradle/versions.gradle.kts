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
