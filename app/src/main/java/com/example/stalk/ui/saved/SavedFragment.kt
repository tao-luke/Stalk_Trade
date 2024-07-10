package com.example.stalk.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stalk.databinding.FragmentSavedBinding
import com.example.stalk.model.SavedPolitician
import com.example.stalk.ui.saved.SavedViewModel

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private val savedViewModel: SavedViewModel by activityViewModels()
    private lateinit var adapter: SavedPoliticianAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SavedPoliticianAdapter(emptyList()) { politician ->
            navigateToPoliticianFragment(politician)
        }
        recyclerView.adapter = adapter

        // Observe saved politicians
        savedViewModel.savedPoliticians.observe(viewLifecycleOwner, { politicians ->
            adapter.updatePoliticians(politicians)
        })

        return root
    }

    private fun navigateToPoliticianFragment(politician: SavedPolitician) {
        val action = SavedFragmentDirections.actionNavigationSavedToPoliticianFragment(
            politician.name,
            politician.profilePictureUrl,
            politician.performance, // Pass the performance
            "saved"
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
