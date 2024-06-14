package com.example.stalk.ui.politician

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.example.stalk.R
import com.example.stalk.databinding.FragmentPoliticianBinding
import com.example.stalk.ui.overviewTable.TableAdapter
import com.example.stalk.ui.overviewTable.TableRowData

class PoliticianFragment : Fragment() {

    private var _binding: FragmentPoliticianBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PoliticianViewModel by viewModels()
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableAdapter: TableAdapter
    private var tableData: MutableList<TableRowData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoliticianBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Enable options menu to handle the up button
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationBell.setOnClickListener {
            // Handle notification bell click
        }

        viewModel.tradeHistory.observe(viewLifecycleOwner) { tradeHistory ->
            // Update the trade history table
            //updateTradeHistoryTable(tradeHistory)
        }

        // Initialize RecyclerView
        tableRecyclerView = binding.tradeHistoryRecyclerview
        tableRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Prepare sample data
        tableData.add(TableRowData("2024-04-23", "AAPL", "Sale", "$1,001 - $15,000", "Shelley M Capito"))
        tableData.add(TableRowData("2024-03-12", "AAPL", "Sale (Full)", "$15,001 - $50,000", "Tommy Tuberville"))
        tableData.add(TableRowData("2024-04-23", "AAPL", "Sale", "$1,001 - $15,000", "Shelley M Capito"))
        tableData.add(TableRowData("2024-03-12", "AAPL", "Sale (Full)", "$15,001 - $50,000", "Tommy Tuberville"))
        tableData.add(TableRowData("2024-04-23", "AAPL", "Sale", "$1,001 - $15,000", "Shelley M Capito"))

        // Initialize and set adapter
        tableAdapter = TableAdapter(tableData)
        tableRecyclerView.adapter = tableAdapter

        // Update the adapter with the sample data
        tableAdapter.notifyDataSetChanged()
    }

    private fun updateTradeHistoryTable(tradeHistory: List<TableRowData>) {
        tableData.clear()
        tableData.addAll(tradeHistory)
        tableAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.navigation_search) // Navigate back to SearchFragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
