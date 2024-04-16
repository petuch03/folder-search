import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import org.junit.jupiter.api.Assertions.*
import petuch03.FileManager

class FileManagerTest {

    private lateinit var fileManager: FileManager

    @BeforeEach
    fun setUp() {
        fileManager = FileManager()
    }

    @Test
    fun testCreateDocumentCorpusWithNonEmptyDirectory(@TempDir tempDir: Path) {
        // Create sample files
        File(tempDir.toFile(), "sample.txt").writeText("Hello, world!")
        File(tempDir.toFile(), "sample.md").writeText("Markdown content")
        // Create an empty file
        File(tempDir.toFile(), "empty.txt").createNewFile()

        val result = fileManager.createDocumentCorpus(tempDir.toFile())
        assertEquals(2, result.size, "Expected to find 2 valid documents")
    }

    @Test
    fun testCreateDocumentCorpusWithEmptyDirectory(@TempDir tempDir: Path) {
        val result = fileManager.createDocumentCorpus(tempDir.toFile())
        assertTrue(result.isEmpty(), "Expected no documents in an empty directory")
    }

    @Test
    fun testCreateDocumentCorpusWithNonExistingDirectory() {
        val nonExistingDir = File("path/that/does/not/exist")
        val result = fileManager.createDocumentCorpus(nonExistingDir)
        assertTrue(result.isEmpty(), "Expected no documents for a non-existing directory")
    }

    @Test
    fun testCreateDocumentCorpusWithUnsupportedFileTypes(@TempDir tempDir: Path) {
        // Create unsupported file types
        File(tempDir.toFile(), "image.jpeg").writeText("This is actually a text in a JPEG file.")
        File(tempDir.toFile(), "sample.exe").writeText("This is an executable.")

        val result = fileManager.createDocumentCorpus(tempDir.toFile())
        assertTrue(result.isEmpty(), "Expected no documents with unsupported file types")
    }
}
