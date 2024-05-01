package petuch03.documents

import java.io.File

class FileManager {
    fun createDocumentCorpus(dir: File): MutableList<Document> {
        val documents = mutableListOf<Document>()
        val validExtensions = setOf("txt", "md", "csv", "json", "xml", "html", "py", "kt", "kts", "java")

        fun traverseDirectory(directory: File) {
            directory.listFiles()?.forEach { file ->
                try {
                    if (file.isDirectory) {
                        traverseDirectory(file)
                    } else if (file.extension in validExtensions) {
                        val content = file.readText(Charsets.UTF_8)
                        if (content.isNotEmpty()) {
                            documents.add(Document(file, content))
                        }
                    }
                } catch (e: Exception) {
                    System.err.println("Error reading ${file.path}: ${e.message}")
                }
            }
        }

        traverseDirectory(dir)
        return documents
    }
}