package com.example.stalk.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R

data class TableRowData(
    val column1: String,
    val column2: String
    // Add more properties for additional columns if needed
)

class TableAdapter(private val tableData: List<TableRowData>) :
    RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val column1TextView: TextView = itemView.findViewById(R.id.column1TextView)
        val column2TextView: TextView = itemView.findViewById(R.id.column2TextView)
        // Add more TextViews or other views for additional columns if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.table_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowData = tableData[position]
        holder.column1TextView.text = rowData.column1
        holder.column2TextView.text = rowData.column2
        // Bind data to additional views for additional columns if needed
    }

    override fun getItemCount(): Int {
        return tableData.size
    }
}
