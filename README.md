The **DBExplorer** library helps you to preview content of SQL tables.

## Download
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23) [![](https://jitpack.io/v/vitoksmile/DBExplorer.svg)](https://jitpack.io/#vitoksmile/DBExplorer)

The first step, add JitPack repository to your root build.gradle file (not module build.gradle file):
```
allprojects {
    repositories {
    ...
        maven { url 'https://jitpack.io' }
    }
}
```
The second step, add the library to your module build.gradle:
```
dependencies {
    implementation 'com.github.vitoksmile:DBExplorer:1.0.0'
}
```

## Usage
You can open a default table previewer or display this information inside your own UI.

**Open DB preview via the library UI.**
Use the next methods to open DB file or show file picker to pick some file from device storage.
```kotlin
object DBExplorerUI {
    fun show(context: Context, localDbName: String)
	
    fun show(context: Context, file: File)
	
    fun show(context: Context, @RawRes resId: Int)
	
    fun showFilePicker(context: Context)
	
    fun showLocalDBPicker(context: Context)
}
```
|   |   |
| ------------ | ------------ |
| <img src="https://github.com/vitoksmile/DBExplorer/blob/master/readme/screen1.png?raw=true" width="270" height="480" />  | <img src="https://github.com/vitoksmile/DBExplorer/blob/master/readme/screen2.png?raw=true" width="270" height="480" />  |

**Load data from DB and display inside your own UI.**
```kotlin
object DBExplorer {
    suspend fun load(context: Context, config: DBConfig): DBResult
}

data class DBConfig(
    val resourceType: ResourceType
)

sealed class ResourceType {
    class LocalDB(val name: String) : ResourceType()

    class FileDB(val file: File) : ResourceType()

    class Stream(val stream: InputStream) : ResourceType()

    class RawResource(@RawRes val resId: Int) : ResourceType()
}
```
All loaded data will be returned inside **DBResult** object:
```kotlin
data class DBResult(
    /**
     * List of table names.
     */
    val tables: List<String>,

    /**
     * Map of table names and all columns for each table.
     */
    val tableColumns: Map<String, List<String>>,

    /**
     * Map of table names and all row's values for each table.
     */
    val tableRows: Map<String, List<List<String>>>
)
```
