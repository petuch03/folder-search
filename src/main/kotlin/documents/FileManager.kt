package petuch03.documents

import java.io.File
import java.nio.file.Files

class FileManager {
    fun createDocumentCorpus(dir: File): MutableList<Document> {
        val documents = mutableListOf<Document>()
        val validExtensions = setOf("txt", "md", "csv", "json", "xml", "html", "py", "kt", "kts", "java")

        fun traverseDirectory(directory: File) {
            val files = directory.listFiles()
            if (files == null || files.isEmpty()) {
                System.err.println("Warning: The directory ${directory.path} is empty or cannot be read.")
                return
            }

            files.forEach { file ->
                try {
                    if (file.isSymbolicLink()) {
                        System.err.println("Skipping symbolic link ${file.path}")
                        return@forEach
                    }

                    if (file.isDirectory) {
                        traverseDirectory(file)
                    } else if (file.extension in validExtensions) {
                        file.bufferedReader(Charsets.UTF_8).use { reader ->
                            val content = StringBuilder()
                            var line = reader.readLine()
                            while (line != null) {
                                content.append(line).append("\n")
                                line = reader.readLine()
                            }
                            if (content.isNotEmpty()) {
                                documents.add(Document(file, content.toString()))
                            }
                        }
                    }
                } catch (e: Exception) {
                    System.err.println("Error reading ${file.path}: ${e.message}")
                }
            }
        }

        traverseDirectory(dir)
        if (documents.isEmpty()) {
            System.err.println("No documents with valid extensions were found in the directory ${dir.path}.")
        }
        return documents
    }

    private fun File.isSymbolicLink(): Boolean = Files.isSymbolicLink(this.toPath())
}