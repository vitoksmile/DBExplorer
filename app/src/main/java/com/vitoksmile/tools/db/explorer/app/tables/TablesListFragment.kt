package com.vitoksmile.tools.db.explorer.app.tables

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.vitoksmile.tools.db.explorer.app.MainViewModel
import com.vitoksmile.tools.db.explorer.app.R
import com.vitoksmile.tools.db.explorer.app.extensions.bind
import com.vitoksmile.tools.db.explorer.app.tables.TablesListFragmentDirections.actionTableView
import kotlinx.android.synthetic.main.fragment_tables_list.*

internal class TablesListFragment : Fragment(R.layout.fragment_tables_list) {
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TablesAdapter(::onTableItemClicked)
        recyclerView.adapter = adapter

        viewModel.tables.bind(this, adapter::setTables)
    }

    private fun onTableItemClicked(tableName: String) {
        findNavController().navigate(actionTableView(tableName))
    }
}
