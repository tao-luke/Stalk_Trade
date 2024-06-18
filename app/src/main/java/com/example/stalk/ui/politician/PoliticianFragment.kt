package com.example.stalk.ui.politician

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.stalk.R
import com.example.stalk.databinding.FragmentPoliticianBinding
import com.example.stalk.ui.overviewTable.TableAdapter
import com.example.stalk.ui.overviewTable.TableRowData
import com.example.stalk.ui.viewmodel.TradeViewModel
import com.example.stalk.model.Trade

class PoliticianFragment : Fragment() {

    private var _binding: FragmentPoliticianBinding? = null
    private val binding get() = _binding!!
    private val tradeViewModel: TradeViewModel by activityViewModels()
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableAdapter: TableAdapter
    private var tableData: MutableList<TableRowData> = mutableListOf()
    private val args: PoliticianFragmentArgs by navArgs()

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

        // Display the politician name and image at the top
        val politicianName = args.politicianName
        val politicianImage = args.politicianImage
        binding.textViewPoliticianName.text = politicianName

        // Load the image using Glide with error handling and transformations
        Glide.with(this)
            .load(politicianImage)
            .transform(CenterCrop(), RoundedCorners(20))
            .error(R.drawable.sample_politician) // Specify the default image resource here
            .into(binding.politicianPicture)

        binding.notificationBell.setOnClickListener {
            // Handle notification bell click
            findNavController().navigate(R.id.navigation_saved)
        }

        // Initialize RecyclerView
        tableRecyclerView = binding.tradeHistoryRecyclerview
        tableRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize and set adapter
        tableAdapter = TableAdapter(tableData)
        tableRecyclerView.adapter = tableAdapter

        // Observe the trades for the politician
        tradeViewModel.trades.observe(viewLifecycleOwner) { tradeHistory ->
            updateTradeHistoryTable(tradeHistory)
        }
    }

    private fun updateTradeHistoryTable(tradeHistory: List<Trade>) {
        tableData.clear()
        tradeHistory.forEach { trade ->
            tableData.add(TableRowData(trade.transactionDate, trade.ticker, trade.type, trade.amount, trade.owner))
        }
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
