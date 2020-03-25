package com.vitoksmile.tools.db.explorer.app.tables

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vitoksmile.tools.db.explorer.app.R
import com.vitoksmile.tools.db.explorer.app.extensions.inflate

internal class TablesAdapter(
    private val onItemClickListener: (tableName: String) -> Unit
) : RecyclerView.Adapter<TablesAdapter.ViewHolder>() {
    private val tables = mutableListOf<String>()

    fun setTables(tables: List<String>) {
        this.tables.apply {
            clear()
            addAll(tables)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        parent.inflate(R.layout.item_table).let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tables[position])
    }

    override fun getItemCount() = tables.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView = view as TextView

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onItemClickListener(tables[position])
            }
        }

        fun bind(tableName: String) {
            textView.text = tableName
        }
    }
}
