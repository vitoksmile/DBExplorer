package com.vitoksmile.tools.db.explorer

import androidx.annotation.RawRes
import java.io.File
import java.io.InputStream

data class DBConfig(
    val resourceType: ResourceType
)

sealed class ResourceType {
    class LocalDB(val name: String) : ResourceType()

    class FileDB(val file: File) : ResourceType()

    class Stream(val stream: InputStream) : ResourceType()

    class RawResource(@RawRes val resId: Int) : ResourceType()
}
