@file:Suppress("unused")

package com.vitoksmile.tools.db.explorer.app

import android.content.Context
import android.content.Intent
import androidx.annotation.RawRes
import com.vitoksmile.tools.db.explorer.app.LaunchMode.*
import java.io.File

object DBExplorerUI {
    /**
     * Show DBExplorer for local DB.
     */
    fun show(context: Context, localDbName: String) {
        show(context, LocalDB(localDbName))
    }

    /**
     * Show DBExplorer for DB file.
     */
    fun show(context: Context, file: File) {
        show(context, FileDB(file))
    }

    /**
     * Show DBExplorer for raw resource.
     */
    fun show(context: Context, @RawRes resId: Int) {
        show(context, RawResource(resId))
    }

    /**
     * Show file picker to choose DB file.
     */
    fun showFilePicker(context: Context) {
        show(context, PickerFile)
    }

    /**
     * Show picker to choose local app's DB.
     */
    fun showLocalDBPicker(context: Context) {
        show(context, PickerLocalDBList)
    }
}

private const val ACTION_LOCAL_DB = "ACTION_LOCAL_DB"
private const val ACTION_FILE_DB = "ACTION_FILE_DB"
private const val ACTION_RAW_RESOURCE = "ACTION_RAW_RESOURCE"
private const val PICKER_FILE = "PICKER_FILE"
private const val PICKER_LOCAL_DB_LIST = "PICKER_LOCAL_DB_LIST"

private fun show(context: Context, launchMode: LaunchMode) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    when (launchMode) {
        is LocalDB -> intent.putExtra(ACTION_LOCAL_DB, launchMode.name)
        is FileDB -> intent.putExtra(ACTION_FILE_DB, launchMode.file.path)
        is RawResource -> intent.putExtra(ACTION_RAW_RESOURCE, launchMode.resId)
        is PickerFile -> intent.putExtra(PICKER_FILE, true)
        is PickerLocalDBList -> intent.putExtra(PICKER_LOCAL_DB_LIST, true)
    }
    context.startActivity(intent)
}

internal fun MainActivity.getLaunchMode(): LaunchMode = when {
    intent.hasExtra(ACTION_LOCAL_DB) ->
        LocalDB(intent.getStringExtra(ACTION_LOCAL_DB)!!)
    intent.hasExtra(ACTION_FILE_DB) ->
        FileDB(File(intent.getStringExtra(ACTION_FILE_DB)!!))
    intent.hasExtra(ACTION_RAW_RESOURCE) ->
        RawResource(intent.getIntExtra(ACTION_RAW_RESOURCE, 0))
    intent.hasExtra(PICKER_FILE) ->
        PickerFile
    intent.hasExtra(PICKER_LOCAL_DB_LIST) ->
        PickerLocalDBList
    else ->
        RawResource(R.raw.sample)
}

internal sealed class LaunchMode {
    class LocalDB(val name: String) : LaunchMode()

    class FileDB(val file: File) : LaunchMode()

    class RawResource(@RawRes val resId: Int) : LaunchMode()

    object PickerFile : LaunchMode()

    object PickerLocalDBList : LaunchMode()
}
