package com.example.stalk.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stalk.databinding.FragmentHomeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.ui.overviewTable.TableAdapter
import com.example.stalk.ui.overviewTable.TableRowData
import androidx.fragment.app.activityViewModels
import com.example.stalk.ui.viewmodel.TradeViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.stalk.model.Trade

class HomeFragment : Fragment(), TableAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableAdapter: TableAdapter
    private var tradeData: MutableList<Trade> = mutableListOf()
    private val tradeViewModel: TradeViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private var fetched: Boolean = false

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

        progressBar = binding.progressBar

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE

        // Initialize RecyclerView
        tableRecyclerView = binding.tableRecyclerView
        tableRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize and set adapter
        tableAdapter = TableAdapter(tradeData, this)
        tableRecyclerView.adapter = tableAdapter

        // Fetch recent trades
        tradeViewModel.fetchRecentTrades(10)
        fetched = true;

        // Prepare table data
        tradeViewModel.trades.observe(this, Observer { trades ->
            // Update the UI with the list of trades
            trades?.let {
                tradeData.clear()
                for (trade in it) {
                    tradeData.add(trade);
                }
                tableAdapter.notifyDataSetChanged()
                if (fetched) {
                    progressBar.visibility = View.GONE
                    fetched = false;
                }
            }

        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(trade: Trade) {
        val action = HomeFragmentDirections.actionHomeFragmentToTransactionDetailsFragment(trade)
        findNavController().navigate(action)
    }
}