package petuch03.index

data class SearchResult(val result: SearchResultEnum, val fileNames: List<String> = emptyList())
