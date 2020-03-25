package com.vitoksmile.tools.db.explorer.app.table

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vitoksmile.tools.db.explorer.app.R
import com.vitoksmile.tools.db.explorer.app.extensions.inflate

internal class TableAdapter : RecyclerView.Adapter<TableAdapter.ViewHolder>() {
    private val values = mutableListOf<String>()

    fun setValues(values: List<String>) {
        this.values.apply {
            clear()
            addAll(values)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        parent.inflate(R.layout.item_table_cell).let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView = view as TextView

        fun bind(value: String) {
            try {
                textView.setBackgroundColor(Color.parseColor(value))
            } catch (error: Throwable) {
                textView.setBackgroundColor(Color.WHITE)
            }
            textView.text = value
        }
    }
}