package com.example.stalk.ui.politician

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.stalk.model.Name
import com.example.stalk.model.SavedPolitician
import com.example.stalk.ui.overviewTable.TableAdapter
import com.example.stalk.ui.viewmodel.TradeViewModel
import com.example.stalk.model.Trade
import com.example.stalk.ui.saved.SavedViewModel
import android.widget.ProgressBar

class PoliticianFragment : Fragment(), TableAdapter.OnItemClickListener {

    private var _binding: FragmentPoliticianBinding? = null
    private val binding get() = _binding!!
    private val tradeViewModel: TradeViewModel by activityViewModels()
    private val politicianViewModel: PoliticianViewModel by activityViewModels()
    private val savedViewModel: SavedViewModel by activityViewModels()
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableAdapter: TableAdapter
    private var tradeData: MutableList<Trade> = mutableListOf()
    private val args: PoliticianFragmentArgs by navArgs()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoliticianBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Enable options menu to handle the up button
        progressBar = binding.progressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE

        // Display the politician name and image at the top
        val politicianName = args.politicianName
        val politicianImage = args.politicianImage
        val performance = args.performance // Get performance from arguments
        val nameParts = politicianName.split(" ")

        val firstName = nameParts[0]
        val lastName = nameParts.drop(1).joinToString(" ") // Handle names with multiple parts

        val politician = Name(firstName, lastName, politicianImage, performance = performance) // Pass performance
        politicianViewModel.setPolitician(politician)

        binding.textViewPoliticianName.text = politicianName

        // Load the image using Glide with error handling and transformations
        Glide.with(this)
            .load(politicianImage)
            .transform(CenterCrop(), RoundedCorners(20))
            .error(R.drawable.sample_politician) // Specify the default image resource here
            .into(binding.politicianPicture)

        // Set the initial state of the bell icon
        updateNotificationIcon(politician.isNotified)

        binding.notificationBell.setOnClickListener {
            politicianViewModel.toggleNotification()
            val savedPolitician = SavedPolitician(
                name = politicianName,
                profilePictureUrl = politicianImage,
                performance = performance // Pass performance
            )
            if (politician.isNotified) {
                savedViewModel.addPolitician(savedPolitician)
            } else {
                savedViewModel.removePolitician(savedPolitician)
            }
        }

        politicianViewModel.politician.observe(viewLifecycleOwner) { updatedPolitician ->
            updateNotificationIcon(updatedPolitician.isNotified)
        }

        // Initialize RecyclerView
        tableRecyclerView = binding.tradeHistoryRecyclerview
        tableRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize and set adapter
        tableAdapter = TableAdapter(tradeData, this)
        tableRecyclerView.adapter = tableAdapter

        // Fetch and observe the trades for the politician
        if (nameParts.size >= 2) {
            tradeViewModel.fetchRecentTradesByName(firstName, lastName)
        }

        tradeViewModel.trades.observe(viewLifecycleOwner) { tradeHistory ->
            tableRecyclerView.visibility = View.GONE
            updateTradeHistoryTable(tradeHistory)
            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.GONE
                tableRecyclerView.visibility = View.VISIBLE
            }, 1000)
        }

        // Display the transaction volume
        tradeViewModel.getTransactionVolume(firstName, lastName).observe(viewLifecycleOwner) { volume ->
            binding.textViewTransactionVolume.text = "Number of Transactions: $volume"
        }

        // Display the performance
        politicianViewModel.politician.observe(viewLifecycleOwner) { updatedPolitician ->
            val performance = when (updatedPolitician.performance) {
                2 -> {
                    binding.textViewPerformanceSign.setTextColor(resources.getColor(android.R.color.holo_green_dark))
                    "++"
                }
                1 -> {
                    binding.textViewPerformanceSign.setTextColor(resources.getColor(android.R.color.holo_green_light))
                    "+"
                }
                else -> {
                    binding.textViewPerformanceSign.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                    "-"
                }
            }
            binding.textViewPerformanceSign.text = performance
        }
    }

    private fun updateNotificationIcon(isNotified: Boolean) {
        if (isNotified) {
            binding.notificationBell.setImageResource(R.drawable.ic_bell_on)
        } else {
            binding.notificationBell.setImageResource(R.drawable.ic_bell_off)
        }
    }

    private fun updateTradeHistoryTable(tradeHistory: List<Trade>) {
        tradeData.clear()
        tradeHistory.forEach { trade ->
            tradeData.add(trade)
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
                val sourceFragment = args.sourceFragment
                when (sourceFragment) {
                    "search" -> findNavController().navigate(R.id.navigation_search)
                    "saved" -> findNavController().navigate(R.id.navigation_saved)
                    else -> findNavController().navigate(R.id.navigation_search)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(trade: Trade) {
        val action = PoliticianFragmentDirections.actionPoliticianFragmentToTransactionDetailsFragment(trade)
        findNavController().navigate(action)
    }
}
