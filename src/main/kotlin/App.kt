package petuch03

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ArgType.Companion.Choice
import kotlinx.cli.default
import kotlinx.cli.required
import petuch03.documents.FileManager
import petuch03.index.BasicIndexManager
import petuch03.index.IndexManager
import petuch03.index.PositionalIndexManager
import java.io.File


class App(private val indexManager: IndexManager) {
    private val fileManager = FileManager()

    fun run(folderPath: String, query: String){
        val corpus = this.fileManager.createDocumentCorpus(File(folderPath))
        this.indexManager.createIndex(corpus)
        val searchResults = this.indexManager.search(query)
        println(searchResults)
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("folder-search")
    val folderPath by parser.option(ArgType.String, shortName = "f", fullName = "path", description = "Path to the folder").required()
    val query by parser.option(ArgType.String, shortName = "q", fullName = "query", description = "Search query").required()
    val indexType by parser.option(ArgType.Choice(listOf("basic", "positional"), {it}), shortName = "t", fullName = "index-type", description = "Type of index manager to use").default("basic")
    parser.parse(args)

    val indexManager = when (indexType) {
        "basic" -> BasicIndexManager()
        "positional" -> PositionalIndexManager()
        else -> throw IllegalArgumentException("Unsupported index manager type")
    }

    App(indexManager).run(folderPath, query)
}