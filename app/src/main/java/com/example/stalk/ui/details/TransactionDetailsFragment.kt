package com.example.stalk.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.stalk.databinding.FragmentTransactionDetailsBinding
import com.example.stalk.ui.viewmodel.TradeViewModel

class TransactionDetailsFragment : Fragment() {

    private var _binding: FragmentTransactionDetailsBinding? = null
    private val binding get() = _binding!!
    private val tradeViewModel: TradeViewModel by activityViewModels()
    private val args: TransactionDetailsFragmentArgs by navArgs()

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

        val trade = args.trade

        binding.apply {
            textFirstName.text = "First Name: ${trade.firstName}"
            textLastName.text = "Last Name: ${trade.lastName}"
            textLink.text = "Link: ${trade.link}"
            textDateReceived.text = "Date Received: ${trade.dateRecieved}"
            textTransactionDate.text = "Transaction Date: ${trade.transactionDate}"
            textOwner.text = "Owner: ${trade.owner}"
            textAssetDescription.text = "Asset Description: ${trade.assetDescription}"
            textType.text = "Type: ${trade.type}"
            textAmount.text = "Amount: ${trade.amount}"
            textSymbol.text = "Ticker: ${trade.ticker}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
