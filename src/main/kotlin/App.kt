package petuch03

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import petuch03.documents.FileManager
import petuch03.index.*
import java.io.File


class App(private val indexManager: IndexManager) {
    private val fileManager = FileManager()

    fun run(folderPath: String, query: String) {
        val corpus = this.fileManager.createDocumentCorpus(File(folderPath))
        this.indexManager.createIndex(corpus)
        val searchResults = this.indexManager.search(query)
        displaySearchResult(folderPath, searchResults)
    }

    private fun displaySearchResult(folderPath: String, result: SearchResult) {
        when (result.result) {
            SearchResultEnum.NO_RESULTS -> println("No results found for your query.")
            SearchResultEnum.SUCCESS -> {
                if (result.fileNames.isEmpty()) {
                    println("No files matched your search query.")
                } else {
                    println("Top search results:")
                    result.fileNames.forEach { fileName ->
                        println("$folderPath/$fileName")
                    }
                }
            }
            SearchResultEnum.QUERY_NOT_TOKENIZED -> println("Query can't be tokenized.")
            SearchResultEnum.ERROR -> println("Error occurred during search.")
        }
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("folder-search")
    val folderPath by parser.option(
        ArgType.String,
        shortName = "f",
        fullName = "path",
        description = "Path to the folder"
    ).required()
    val query by parser.option(ArgType.String, shortName = "q", fullName = "query", description = "Search query")
        .required()
    val indexType by parser.option(
        ArgType.Choice(listOf("basic", "positional"), { it }),
        shortName = "t",
        fullName = "index-type",
        description = "Type of index manager to use"
    ).default("basic")
    parser.parse(args)

    require(File(folderPath).exists()) { println("The directory $folderPath does not exist."); return }
    require(File(folderPath).isDirectory) { println("The provided path $folderPath is not a directory."); return }

    val indexManager = when (indexType) {
        "basic" -> BasicIndexManager()
        "positional" -> PositionalIndexManager()
        else -> throw IllegalArgumentException("Unsupported index manager type")
    }

    App(indexManager).run(folderPath, query)
}