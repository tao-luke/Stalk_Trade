package com.example.stalk.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stalk.databinding.FragmentSearchBinding
import com.example.stalk.ui.viewmodel.TradeViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    private val tradeViewModel: TradeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observing LiveData from SearchViewModel
        searchViewModel.text.observe(viewLifecycleOwner, Observer { newText ->
            binding.textView.text = newText
        })

        searchViewModel.searchQuery.observe(viewLifecycleOwner, Observer { query ->
            // Handle search query update if needed
        })

        // Handling button click to navigate to TransactionDetailsFragment
        binding.buttonTransactionDetails.setOnClickListener {
            val firstTrade = tradeViewModel.trades.value?.firstOrNull()
            if (firstTrade != null) {
                val action = SearchFragmentDirections
                    .actionNavigationSearchToTransactionDetailsFragment()
                findNavController().navigate(action)
            } else {
                // Handle case when there are no trades available
                // Example: Toast.makeText(requireContext(), "No trades available", Toast.LENGTH_SHORT).show()
            }
        }

        // Handling the original search button click
        binding.buttonSearch.setOnClickListener {
            searchViewModel.updateText("You searched for someone!")
        }

        // Listen for text changes in the search bar
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchViewModel.updateSearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
