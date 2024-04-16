import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import petuch03.IndexManager
import java.io.File

class IndexManagerTest {

    private lateinit var indexManager: IndexManager
    private val sampleCorpus = mutableListOf(
        Pair(File("file1.txt"), "Hello world. Welcome to Kotlin."),
        Pair(File("file2.txt"), "Hello Kotlin! Learn Kotlin and world."),
        Pair(File("file3.txt"), "This is an empty test case.")
    )

    @BeforeEach
    fun setUp() {
        indexManager = IndexManager()
        indexManager.createIndex(sampleCorpus)
        indexManager.createPositionalIndex(sampleCorpus)
    }

    @Test
    fun testSimpleSearchWithValidQuery() {
        val results = indexManager.search("Kotlin")
        assertTrue(results.size == 2 && (results["file2.txt"] ?: 0) > (results["file1.txt"] ?: 0), "File2 should have more occurrences of 'Kotlin' than File1.")
    }

    @Test
    fun testSimpleSearchEmptyQuery() {
        val results = indexManager.search("")
        assertTrue(results.isEmpty(), "Expected no results for empty query.")
    }

    @Test
    fun testSimpleSearchQueryOnlySpaces() {
        val results = indexManager.search("    ")
        assertTrue(results.isEmpty(), "Expected no results for a query of only spaces.")
    }

    @Test
    fun testSimpleSearchNonExistentToken() {
        val results = indexManager.search("nonexistent")
        assertTrue(results.isEmpty(), "Expected no results for a non-existent token.")
    }

    @Test
    fun testExactMatchWithValidSequence() {
        val results = indexManager.searchExactMatch("Hello world")
        assertTrue("file1.txt" in results, "Expected 'file1.txt' to contain the exact sequence 'Hello world'.")
    }

    @Test
    fun testExactMatchEmptyQuery() {
        val results = indexManager.searchExactMatch("")
        assertTrue(results.isEmpty(), "Expected no results for empty query.")
    }

    @Test
    fun testExactMatchQueryOnlySpaces() {
        val results = indexManager.searchExactMatch("     ")
        assertTrue(results.isEmpty(), "Expected no results for a query of only spaces.")
    }

    @Test
    fun testExactMatchNoExactMatches() {
        val results = indexManager.searchExactMatch("Welcome to world")
        assertTrue(results.isEmpty(), "Expected no results when no exact sequence matches.")
    }
}
