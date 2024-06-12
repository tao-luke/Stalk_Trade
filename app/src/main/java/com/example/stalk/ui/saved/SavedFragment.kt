package com.example.stalk.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stalk.R
import com.example.stalk.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var savedViewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedViewModel = ViewModelProvider(this).get(SavedViewModel::class.java)

        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val politicians = listOf(
            Politician("John Doe", R.drawable.ic_profile_placeholder, "Bought stock in XYZ"),
            Politician("Jane Smith", R.drawable.ic_profile_placeholder, "Sold stock in ABC"),
            Politician("Alice Johnson", R.drawable.ic_profile_placeholder, "Bought stock in DEF"),
            Politician("Robert Brown", R.drawable.ic_profile_placeholder, "Sold stock in GHI"),
            Politician("Emily Davis", R.drawable.ic_profile_placeholder, "Bought stock in JKL"),
            Politician("Michael Wilson", R.drawable.ic_profile_placeholder, "Sold stock in MNO"),
            Politician("Sophia Martinez", R.drawable.ic_profile_placeholder, "Bought stock in PQR"),
            Politician("James Anderson", R.drawable.ic_profile_placeholder, "Sold stock in STU"),
            Politician("Isabella Thomas", R.drawable.ic_profile_placeholder, "Bought stock in VWX"),
            Politician("William Jackson", R.drawable.ic_profile_placeholder, "Sold stock in YZ")
        )
        val adapter = SavedPoliticianAdapter(politicians) { politician ->
            // Handle profile button click
        }
        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext())
        recyclerView.addItemDecoration(dividerItemDecoration)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
