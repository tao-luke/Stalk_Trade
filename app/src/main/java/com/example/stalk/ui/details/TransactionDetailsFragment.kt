package com.example.stalk.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stalk.databinding.FragmentTransactionDetailsBinding
import com.example.stalk.ui.viewmodel.TradeViewModel

class TransactionDetailsFragment : Fragment() {

    private var _binding: FragmentTransactionDetailsBinding? = null
    private val binding get() = _binding!!
    private val tradeViewModel: TradeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tradeViewModel.trades.observe(viewLifecycleOwner, { trades ->
            val firstTrade = trades.firstOrNull()
            firstTrade?.let { trade ->
                binding.textFirstName.text = "First Name: ${trade.firstName}"
                binding.textLastName.text = "Last Name: ${trade.lastName}"
                binding.textOffice.text = "Office: ${trade.office}"
                binding.textLink.text = "Link: ${trade.link}"
                binding.textDateReceived.text = "Date Received: ${trade.dateReceived}"
                binding.textTransactionDate.text = "Transaction Date: ${trade.transactionDate}"
                binding.textOwner.text = "Owner: ${trade.owner}"
                binding.textAssetDescription.text = "Asset Description: ${trade.assetDescription}"
                binding.textAssetType.text = "Asset Type: ${trade.assetType}"
                binding.textType.text = "Type: ${trade.type}"
                binding.textAmount.text = "Amount: ${trade.amount}"
                binding.textComment.text = "Comment: ${trade.comment}"
                binding.textSymbol.text = "Symbol: ${trade.symbol}"
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
