package photos

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.File
import javax.servlet.ServletContext

class LocalContent(path: String?) {
  constructor(servletContext: ServletContext): this(servletContext.getRealPath("content"))

  private val albums: Map<String, String>

  private val mdParser = Parser.builder().build()
  private val mdRenderer = HtmlRenderer.builder().build()

  init {
    albums = if (path != null) File(path)
        .listFiles { file -> file.name.endsWith(".md") }
        .map { Pair(it.name.substringBefore('.'), markdown2Html(it.readText())) }
        .toMap()
    else emptyMap()
  }

  private fun markdown2Html(source: String): String {
    val document = mdParser.parse(source)
    return mdRenderer.render(document)
  }

  fun contains(albumName: String?) = albums.contains(albumName)

  fun forAlbum(name: String?) = albums[name]
}