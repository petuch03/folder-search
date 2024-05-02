package petuch03.index

import petuch03.documents.Document

class BasicIndexManager : IndexManager {
    private val index = mutableMapOf<String, MutableMap<String, Int>>()

    override fun createIndex(corpus: List<Document>) {
        corpus.forEach { (file, content) ->
            tokenize(content).forEach { token ->
                val fileMap = this.index.getOrPut(token) { mutableMapOf() }
                fileMap[file] = fileMap.getOrDefault(file, 0) + 1
            }
        }
    }

    override fun search(query: String): SearchResult {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return SearchResult(SearchResultEnum.QUERY_NOT_TOKENIZED)

        val results = mutableMapOf<String, Int>()
        tokens.forEach { token ->
            this.index[token]?.forEach { (file, count) ->
                results[file] = results.getOrDefault(file, 0) + count
            }
        }
        val fileNames = results
            .toList()
            .sortedByDescending { it.second }
            .map { it.first }
            .take(5)
        return SearchResult(SearchResultEnum.SUCCESS, fileNames)
    }
}