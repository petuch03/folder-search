package petuch03.index

import petuch03.documents.Document

class BasicIndexManager : IndexManager {
    private val index = mutableMapOf<String, MutableMap<String, Int>>()

    override fun createIndex(corpus: List<Document>) {
        corpus.forEach { (file, content) ->
            tokenize(content).forEach { token ->
                val fileMap = this.index.getOrPut(token) { mutableMapOf() }
                fileMap[file.name] = fileMap.getOrDefault(file.name, 0) + 1
            }
        }
    }

    override fun search(query: String): List<String> {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return emptyList()

        val results = mutableMapOf<String, Int>()
        tokens.forEach { token ->
            this.index[token]?.forEach { (file, count) ->
                results[file] = results.getOrDefault(file, 0) + count
            }
        }
        return results
            .toList()
            .sortedByDescending { it.second }
            .map { it.first }
            .take(5)
    }
}