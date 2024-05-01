package petuch03.index

import petuch03.documents.Document
import java.util.*

interface IndexManager {
    fun createIndex(corpus: List<Document>)
    fun search(query: String): List<String>
    fun tokenize(text: String): List<String> {
        return text.lowercase(Locale.getDefault())
            .replace(Regex("[^a-zA-Z0-9_]+"), " ")
            .split("\\s+".toRegex())
            .filter { it.isNotEmpty() }
    }
}