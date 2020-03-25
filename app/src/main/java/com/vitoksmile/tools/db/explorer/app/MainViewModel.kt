package com.vitoksmile.tools.db.explorer.app

import android.app.Application
import androidx.annotation.RawRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vitoksmile.tools.db.explorer.DBConfig
import com.vitoksmile.tools.db.explorer.DBExplorer
import com.vitoksmile.tools.db.explorer.DBResult
import com.vitoksmile.tools.db.explorer.ResourceType
import com.vitoksmile.tools.db.explorer.ResourceType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

internal class MainViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var dbResult: DBResult? = null

    private val _tables = MutableLiveData<List<String>>()
    val tables: LiveData<List<String>> = _tables

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    val closeApp = MutableLiveData<Unit>()

    fun load(localDbName: String) {
        load(LocalDB(localDbName))
    }

    fun load(file: File) {
        load(FileDB(file))
    }

    fun load(stream: InputStream) {
        load(Stream(stream))
    }

    fun load(@RawRes resId: Int) {
        load(RawResource(resId))
    }

    fun findAllLocalDBNames() = DBExplorer.findAllLocalDBNames(getApplication())

    fun getColumns(tableName: String): List<String>? {
        return dbResult?.tableColumns?.get(tableName)
    }

    fun getRows(tableName: String): List<List<String>>? {
        return dbResult?.tableRows?.get(tableName)
    }

    private fun load(resourceType: ResourceType) = viewModelScope.launch {
        _loading.value = true
        val result = withContext(Dispatchers.IO) {
            val config = DBConfig(resourceType)
            DBExplorer.load(getApplication(), config).also { dbResult = it }
        }
        _tables.value = result.tables
        _loading.value = false
    }
}
