package com.vitoksmile.tools.db.explorer

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.annotation.RawRes
import com.vitoksmile.tools.db.explorer.ResourceType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

object DBExplorer {
    /**
     * Load DB info.
     */
    suspend fun load(context: Context, config: DBConfig): DBResult {
        return when (val resourceType = config.resourceType) {
            is LocalDB -> loadLocal(context, resourceType.name)
            is FileDB -> loadFile(context, resourceType.file)
            is Stream -> loadStream(context, resourceType.stream)
            is RawResource -> loadRawResources(context, resourceType.resId)
        }
    }

    /**
     * Find all local DB names to load them via [load] with [ResourceType.LocalDB].
     */
    fun findAllLocalDBNames(context: Context): List<String> {
        return context.databaseList().toList()
    }

    /**
     * Load local DB.
     */
    private suspend fun loadLocal(
        context: Context,
        name: String
    ): DBResult = withContext(Dispatchers.IO) {
        val db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)

        val tables = loadTableNames(db)
        Log.d("DBExplorer_", "tables: $tables")

        val tableColumns = HashMap<String, List<String>>()
        for (table in tables) {
            tableColumns[table] = loadColumnNames(db, table)
        }
        Log.d("DBExplorer_", "tableColumns: $tableColumns")

        val tableRows = HashMap<String, List<List<String>>>()
        for (tableName in tables) {
            tableRows[tableName] = loadRows(db, tableName)
        }
        Log.d("DBExplorer_", "tableRows: $tableRows")

        DBResult(tables, tableColumns, tableRows)
    }

    /**
     * Load DB from the [file].
     */
    private suspend fun loadFile(
        context: Context,
        file: File
    ): DBResult = withContext(Dispatchers.IO) {
        val name = "com.vitoksmile.tools.db.explorer_file_" +
                file.path.hashCode()
        val input = file.inputStream()
        copyToDBFolder(context, input, name)
        loadLocal(context, name)
    }

    /**
     * Load DB from the [stream].
     */
    private suspend fun loadStream(
        context: Context,
        stream: InputStream
    ): DBResult {
        val name = "com.vitoksmile.tools.db.explorer_file_" +
                System.currentTimeMillis().toString()
        copyToDBFolder(context, stream, name)
        return loadLocal(context, name)
    }

    /**
     * Load DB from raw resources.
     */
    private suspend fun loadRawResources(context: Context, @RawRes resId: Int): DBResult {
        val name = "com.vitoksmile.tools.db.explorer_resources_raw_" +
                context.resources.getResourceEntryName(resId)
        val input = context.resources.openRawResource(resId)
        copyToDBFolder(context, input, name)
        return loadLocal(context, name)
    }

    private suspend fun copyToDBFolder(
        context: Context,
        input: InputStream,
        name: String
    ) = withContext(Dispatchers.IO) {
        val db = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)
        db.close()

        val output = File(db.path).outputStream()
        output.use {
            input.use {
                input.copyTo(output)
            }
        }
    }

    private suspend fun loadTableNames(
        db: SQLiteDatabase
    ): List<String> = withContext(Dispatchers.IO) {
        val tableNames = mutableListOf<String>()

        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        cursor.use {
            val column = cursor.columnNames.first()
            val columnIndex = cursor.getColumnIndex(column)

            while (cursor.moveToNext()) {
                val tableName = cursor.getString(columnIndex)
                tableNames.add(tableName)
            }
        }

        tableNames
    }

    private suspend fun loadColumnNames(
        db: SQLiteDatabase,
        tableName: String
    ): List<String> = withContext(Dispatchers.IO) {
        val columnNames = mutableListOf<String>()

        val cursor = db.rawQuery("SELECT * FROM $tableName WHERE 0", null)
        cursor.use {
            columnNames.addAll(cursor.columnNames.toList())
        }

        columnNames
    }

    private suspend fun loadRows(
        db: SQLiteDatabase,
        tableName: String
    ): List<List<String>> = withContext(Dispatchers.IO) {
        val cursor = db.rawQuery("SELECT * FROM $tableName", null)
        val columnIndexes = cursor.columnNames.map { cursor.getColumnIndex(it) }.toTypedArray()
        val rows = mutableListOf<List<String>>()
        cursor.use {
            while (cursor.moveToNext()) {
                val rowValues = mutableListOf<String>()
                for (columnIndex in columnIndexes) {
                    when (cursor.getType(columnIndex)) {
                        Cursor.FIELD_TYPE_NULL ->
                            rowValues.add("null")
                        Cursor.FIELD_TYPE_INTEGER ->
                            rowValues.add(cursor.getInt(columnIndex).toString())
                        Cursor.FIELD_TYPE_FLOAT ->
                            rowValues.add(cursor.getFloat(columnIndex).toString())
                        Cursor.FIELD_TYPE_STRING ->
                            rowValues.add(cursor.getString(columnIndex))
                        Cursor.FIELD_TYPE_BLOB ->
                            rowValues.add("blob")
                    }
                }
                rows.add(rowValues)
            }
        }
        rows
    }
}
