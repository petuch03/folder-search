package petuch03

import java.io.File
import java.util.*

class IndexManager {
    private val index = mutableMapOf<String, MutableMap<String, Int>>()
    private val positionalIndex = mutableMapOf<String, MutableMap<String, MutableList<Int>>>()

    fun createIndex(corpus: MutableList<Pair<File, String>>) {
        corpus.forEach { (file, content) ->
            tokenize(content).forEach { token ->
                // Initialize the map for this token if it doesn't exist
                val fileMap = index.getOrPut(token) { mutableMapOf() }
                // Increment the count of this token in the current file
                fileMap[file.name] = fileMap.getOrDefault(file.name, 0) + 1
            }
        }
    }

    fun search(query: String): Map<String, Int> {
        val results = mutableMapOf<String, Int>()
        tokenize(query).forEach { token ->
            this.index[token]?.forEach { (file, count) ->
                results[file] = results.getOrDefault(file, 0) + count
            }
        }
        return results.toList().sortedByDescending { it.second }.toMap()
    }


    private fun tokenize(text: String): List<String> {
        return text.lowercase(Locale.getDefault())
            .replace(Regex("[^a-zA-Z0-9_]+"), " ")
            .split("\\s+".toRegex())
            .filter { it.isNotEmpty() }
    }

    fun createPositionalIndex(corpus: MutableList<Pair<File, String>>) {
        corpus.forEach { (file, content) ->
            tokenize(content).forEachIndexed { position, token ->
                this.positionalIndex.getOrPut(token) { mutableMapOf() }
                    .getOrPut(file.name) { mutableListOf() }
                    .add(position)
            }
        }
    }

    fun searchExactMatch(query: String): List<String> {
        val tokens = tokenize(query)
        if (tokens.isEmpty()) return emptyList()

        // Retrieve the sets of files that contain each token.
        val fileLists = tokens.map { positionalIndex[it]?.keys ?: emptySet() }
        // Find common files that contain all tokens.
        val commonFiles = fileLists.reduce { acc, set -> acc.intersect(set) }

        // Find exact matches in these files.
        return commonFiles.filter { file ->
            // Extract positions for each token in the file, adjusting for index to align sequences
            val positionsLists = tokens.mapIndexed { index, token ->
                positionalIndex[token]?.get(file)?.map { it - index }
            }

            if (positionsLists.any { it == null }) return@filter false

            // Intersect position lists to find exact sequence matches
            positionsLists.filterNotNull().reduce { acc, positions ->
                acc.intersect(positions).toList()  // Reduce by intersecting lists, converting to List for safety
            }.isNotEmpty()
        }.toList()
    }

    fun getIndex(): MutableMap<String, MutableMap<String, Int>> {
        return this.index
    }

}