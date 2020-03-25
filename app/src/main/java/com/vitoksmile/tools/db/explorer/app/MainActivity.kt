package com.vitoksmile.tools.db.explorer.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.vitoksmile.tools.db.explorer.app.LaunchMode.*
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (val mode = getLaunchMode()) {
            is LocalDB -> viewModel.load(mode.name)
            is FileDB -> viewModel.load(mode.file)
            is RawResource -> viewModel.load(mode.resId)
            is PickerFile -> showFilePicker()
            is PickerLocalDBList -> showLocalDBPicker()
        }

        viewModel.loading.observe(this, Observer {
            loading_view.isVisible = it
        })
        viewModel.closeApp.observe(this, Observer {
            finish()
        })
    }

    private fun showFilePicker() {
        prepareCall(OpenFileContract()) { uri ->
            val stream = uri?.let { contentResolver.openInputStream(it) } ?: return@prepareCall
            viewModel.load(stream)
        }.launch(Unit)
    }

    private fun showLocalDBPicker() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.actionLocalDBListDialog)
    }
}

class OpenFileContract : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(input: Unit) = Intent(Intent.ACTION_OPEN_DOCUMENT)
        .setType("*/*")
        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        .addCategory(Intent.CATEGORY_OPENABLE)

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode != Activity.RESULT_OK) return null
        return intent?.data
    }
}
