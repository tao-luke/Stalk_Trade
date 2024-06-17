package com.example.stalk.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stalk.R
import com.example.stalk.databinding.FragmentSearchBinding
import com.example.stalk.ui.viewmodel.NameViewModel
import com.example.stalk.ui.viewmodel.SearchViewModel
import com.example.stalk.ui.viewmodel.TradeViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    private val tradeViewModel: TradeViewModel by activityViewModels()
    private val nameViewModel: NameViewModel by activityViewModels()

    private lateinit var nameAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the AutoCompleteTextView adapter
        nameAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf())
        binding.searchEditText.setAdapter(nameAdapter)

        // Observe names from NameViewModel
        nameViewModel.names.observe(viewLifecycleOwner, Observer { names ->
            val nameStrings = names.map { "${it.firstName} ${it.lastName}" }
            nameAdapter.clear()
            nameAdapter.addAll(nameStrings)
            nameAdapter.notifyDataSetChanged()
        })

        // Fetch names from the repository
        nameViewModel.fetchNames()

        // Observe text from SearchViewModel
        searchViewModel.text.observe(viewLifecycleOwner, Observer { newText ->
            binding.textView.text = newText
        })

        // Handling the original search button click
        binding.buttonSearch.setOnClickListener {
            val searchName = binding.searchEditText.text.toString()
            val nameParts = searchName.split(" ")
            if (nameParts.size == 2) {
                val firstName = nameParts[0]
                val lastName = nameParts[1]

                // Fetch trades for the given name
                tradeViewModel.fetchRecentTradesByName(firstName, lastName, 10)

                // Observe the trades and navigate once they are fetched
                tradeViewModel.trades.observe(viewLifecycleOwner, Observer { trades ->
                    if (trades.isNotEmpty()) {
                        // Navigate to PoliticianFragment with the name as an argument
                        val action = SearchFragmentDirections.actionSearchFragmentToPoliticianFragment(searchName)
                        findNavController().navigate(action)
                    } else {
                        // Handle case when there are no trades available
                        // Example: Toast.makeText(requireContext(), "No trades available", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Handle case where the name is not in the correct format
                // Example: Toast.makeText(requireContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show()
            }
        }

        // Listen for text changes in the search bar
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchViewModel.updateSearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set an OnEditorActionListener to trigger the search button click when "Enter" is pressed
        binding.searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.buttonSearch.performClick()
                return@OnEditorActionListener true
            }
            false
        })
        // TODO! we need to add a char limit on the search box!
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
