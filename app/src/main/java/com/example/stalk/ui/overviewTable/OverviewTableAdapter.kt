package com.example.stalk.ui.overviewTable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R

data class TableRowData(
    val column1: String,
    val column2: String,
    val column3: String,
    val column4: String,
    val column5: String,
    // Add more properties for additional columns if needed
)

class TableAdapter(private val tableData: List<TableRowData>) :
    RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateCol)
        val tickerTextView: TextView = itemView.findViewById(R.id.tickerCol)
        val typeTextView: TextView = itemView.findViewById(R.id.typeCol)
        val amountTextView: TextView = itemView.findViewById(R.id.amountCol)
        val nameTextView: TextView = itemView.findViewById(R.id.nameCol)
        // Add more TextViews or other views for additional columns if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.table_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowData = tableData[position]
        holder.dateTextView.text = rowData.column1
        holder.tickerTextView.text = rowData.column2
        holder.typeTextView.text = rowData.column3
        holder.amountTextView.text = rowData.column4
        holder.nameTextView.text = rowData.column5
        // Bind data to additional views for additional columns if needed
    }

    override fun getItemCount(): Int {
        return tableData.size
    }
}
