package com.vitoksmile.tools.db.explorer.app.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vitoksmile.tools.db.explorer.app.MainViewModel
import com.vitoksmile.tools.db.explorer.app.R
import com.vitoksmile.tools.db.explorer.app.extensions.inflate

internal class LocalDBListDialog : DialogFragment() {
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_local_db_list, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = DBAdapter(viewModel.findAllLocalDBNames()) { dbName ->
            viewModel.load(dbName)
            dismiss()
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.closeApp.value = Unit
    }
}

private class DBAdapter(
    private val list: List<String>,
    private val onItemClickListener: (dbName: String) -> Unit
) : RecyclerView.Adapter<DBAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        parent.inflate(R.layout.item_local_db).let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView = view as TextView

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onItemClickListener(list[position])
            }
        }

        fun bind(value: String) {
            textView.text = value
        }
    }
}
