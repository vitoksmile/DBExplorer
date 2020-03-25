package com.vitoksmile.tools.db.explorer

data class DBResult(
    /**
     * List of table names.
     */
    val tables: List<String>,

    /**
     * Map of table names and all columns for each table.
     */
    val tableColumns: Map<String, List<String>>,

    /**
     * Map of table names and all row's values for each table.
     */
    val tableRows: Map<String, List<List<String>>>
)
