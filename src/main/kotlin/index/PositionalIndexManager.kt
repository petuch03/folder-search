package petuch03.index

import petuch03.documents.Document

class PositionalIndexManager : IndexManager {
    private val positionalIndex = mutableMapOf<String, MutableMap<String, MutableList<Int>>>()

    override fun createIndex(corpus: List<Document>) {
        corpus.forEach { (file, content) ->
            tokenize(content).forEachIndexed { position, token ->
                val fileMap = this.positionalIndex.getOrPut(token) { mutableMapOf() }
                val oldList = fileMap.getOrDefault(file.name, mutableListOf())
                oldList.add(position)
                fileMap[file.name] = oldList
            }
        }
    }

    override fun search(query: String): SearchResult {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return SearchResult(SearchResultEnum.NO_RESULTS)

        val commonFiles = tokens // Tokens from query
            .map { positionalIndex[it]?.keys ?: emptySet() } //List<Set<String>>, where each Set<String> contains the files in which a particular token appears
            .reduce { acc, set -> acc.intersect(set) } //Set<String>, which contains only those files that include all the tokens from the original list


        val fileNames = commonFiles.filter { file ->
            val positionOffsetsLists = tokens.mapIndexed { index, token ->
                positionalIndex[token]
                    ?.get(file)
                    ?.map { it - index } // adjusts these so that if the tokens appear consecutively in the file, their adjusted positions will match
                    ?.distinct()
            } // List<List<Int>?>, which contains position offsets (1 dimension) of each token (0 dimension) in file

            if (positionOffsetsLists.any { it == null }) return@filter false

            positionOffsetsLists.filterNotNull()
                .reduce { acc, positions -> acc.intersect(positions.toSet()).toList() // Filters for same position offset of token in single file
            }.isNotEmpty()
        }.toList()
        return SearchResult(SearchResultEnum.SUCCESS, fileNames)
    }
}