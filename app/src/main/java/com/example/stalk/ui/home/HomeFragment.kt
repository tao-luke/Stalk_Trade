package com.example.stalk.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stalk.databinding.FragmentHomeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.ui.overviewTable.TableAdapter
import com.example.stalk.ui.overviewTable.TableRowData

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableAdapter: TableAdapter
    private var tableData: MutableList<TableRowData> = mutableListOf()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        tableRecyclerView = binding.tableRecyclerView
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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}