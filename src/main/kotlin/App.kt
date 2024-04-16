package petuch03

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import java.io.File


class App {
    val fileManager = FileManager()
    val indexManager = IndexManager()
}

fun main(args: Array<String>) {
    val parser = ArgParser("folder-search")
    val folderPath by parser.option(ArgType.String, shortName = "f", description = "Path to the folder").required()
    val query by parser.option(ArgType.String, shortName = "q", description = "Search query").required()
    parser.parse(args)

    val app = App()
    val corpus = app.fileManager.createDocumentCorpus(File(folderPath))
    app.indexManager.createIndex(corpus)
    app.indexManager.createPositionalIndex(corpus)
    val searchResults = app.indexManager.search(query)
    println(searchResults)
    val searchExactResults = app.indexManager.searchExactMatch(query)
    println(searchExactResults)

}

