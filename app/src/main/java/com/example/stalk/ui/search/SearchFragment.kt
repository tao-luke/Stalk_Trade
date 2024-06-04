package com.example.stalk.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stalk.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observing LiveData from ViewModel
        searchViewModel.text.observe(viewLifecycleOwner, Observer { newText ->
            // Update UI with new text
            binding.textView.text = newText
        })

        searchViewModel.searchQuery.observe(viewLifecycleOwner, Observer { query ->
            // Handle search query update (todo!)
            // For example, filter a list based on the query
        })

        // Handling button click to update ViewModel
        binding.button.setOnClickListener {
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
        // We should add click away zoom out as well
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
