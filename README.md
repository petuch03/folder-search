## Overview

Folder-Search-CLI is a command-line tool that indexes text files within a specified directory and allows users to perform searches for string occurrences. The tool supports various file types and provides detailed search results, including the relative path from the base directory.

## Requirements

- `Java 11` or higher

## Configuration

No specific configuration like API keys or environment variables is required for this tool.

## Usage Syntax:
```bash
java -jar folder-serach.jar -d <path_to_directory> -q <search_query> [-t <basic/positional>]
```
- `-d`, `--directory`: Path to the directory containing text files to be indexed.
- `-q`, `--query`: Search query to find in the indexed documents.
- `-t`, `--index-type`: Optional. Specifies what index to use (`positional` or `basic`). Default value is `basic`.


## Example:
```bash
java -jar build/libs/folder-search.jar -d src/main -q "SearchResultEnum NO_RESULTS"
```
This command will index the text files in the `src/main` directory and search for occurrences of `"SearchResultEnum NO_RESULTS"`.

## Output

The tool will display the top search results, listing the relative paths of files where the query string appears. If no matches are found, it will inform the user accordingly.

If there are errors during execution, such as an invalid directory path or inaccessible files, the tool will print an error message and terminate.