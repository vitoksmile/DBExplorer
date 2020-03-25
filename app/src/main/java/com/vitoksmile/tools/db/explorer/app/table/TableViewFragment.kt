package com.vitoksmile.tools.db.explorer.app.table

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vitoksmile.tools.db.explorer.app.MainViewModel
import com.vitoksmile.tools.db.explorer.app.R
import kotlinx.android.synthetic.main.fragment_tableview.*

internal class TableViewFragment : Fragment(R.layout.fragment_tableview) {
    private val viewModel by activityViewModels<MainViewModel>()

    private val args by navArgs<TableViewFragmentArgs>()
    private inline val tableName get() = args.tableName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = tableName
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        val spanCount = viewModel.getColumns(tableName)?.size ?: return
        val layoutManager = GridLayoutManager(requireContext(), spanCount)

        val adapter = TableAdapter()
        val values = mutableListOf<String>()
        values.addAll(viewModel.getColumns(tableName) ?: return)
        viewModel.getRows(tableName)?.forEach { row ->
            row.forEach { column ->
                values.add(column)
            }
        } ?: return
        adapter.setValues(values)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}
