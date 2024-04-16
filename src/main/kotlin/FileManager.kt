package petuch03

import java.io.File

class FileManager {
    /**
     * Recursively collects all text files in the given directory and its subdirectories.
     * Returns a list of Pair<File, String> where File is the file reference and String is the content of the file.
     *
     * @param dir the directory to start the search.
     * @return a list of pairs of file and its content.
     */
    fun createDocumentCorpus(dir: File): MutableList<Pair<File, String>> {
        val documents = mutableListOf<Pair<File, String>>()
        val validExtensions = setOf("txt", "md", "csv", "json", "xml", "html", "py", "kt", "kts", "java") // Add or remove file types as necessary

        fun traverseDirectory(directory: File) {
            directory.listFiles()?.forEach { file ->
                try {
                    if (file.isDirectory) {
                        traverseDirectory(file)
                    } else if (file.extension in validExtensions) {
                        val content = file.readText(Charsets.UTF_8)
                        if (content.isNotEmpty()) {
                            documents.add(file to content)
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
