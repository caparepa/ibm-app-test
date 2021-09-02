package com.example.international.business.men.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.databinding.FragmentTransactionListBinding
import com.example.international.business.men.ui.adapter.base.DynamicAdapter
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.TransactionItemModel
import com.example.international.business.men.ui.adapter.type.factory.TransactionItemTypeFactoryImpl
import com.example.international.business.men.ui.viewmodel.ProductTransactionViewModel
import com.example.international.business.men.utils.toKotlinObject
import com.example.international.business.men.utils.toastLong
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
/**
 * A simple [Fragment] subclass.
 * Use the [TransactionListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionListFragment : Fragment(), KoinComponent {

    private val productTransactionViewModel: ProductTransactionViewModel by viewModel()

    private var _binding : FragmentTransactionListBinding? = null
    private val binding get() = _binding!!

    private var sku: String? = null
    private var totalAmount: String? = null

    private var allTransactionList: List<TransactionItem>? = null
    private var exchangeRatelist: List<ExchangeRateItem>? = null

    private var transactionAdapter: DynamicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sku = it.getString("sku").toString()
            allTransactionList = it.getParcelableArrayList<TransactionItem>("transactionList")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        observeViewModel()
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sku?.let {
            loadData(it)
        }
        allTransactionList?.let {
            requireActivity().toastLong("HAI!")

            setUpTransactionListAdapter(it)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadData(sku: String) {
        //productTransactionViewModel.getTransactionList()
        productTransactionViewModel.getExchangeRateList()
    }

    private fun observeViewModel() = productTransactionViewModel.run {
        transactionList.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()) {
                requireActivity().toastLong("TX - DATA!")
                setUpTransactionListAdapter(it)
            } else {
                requireActivity().toastLong("TX - NO DATA!")
            }
        })
        exchangeRateList.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()) {
                requireActivity().toastLong("EX - DATA!")
            } else {
                requireActivity().toastLong("EX - NO DATA!")
            }
        })
    }

    private fun setUpTransactionListAdapter(list: List<TransactionItem>) {
        transactionAdapter = getTransactionListAdapter(list)
        binding.rvTransactionList.adapter = transactionAdapter
        binding.rvTransactionList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getTransactionListAdapter(list: List<TransactionItem>): DynamicAdapter {
        return DynamicAdapter(
            typeFactory = TransactionItemTypeFactoryImpl(),
            items = getListForAdapter(list.toMutableList()),
            onClick = onItemClick
        )
    }

    private fun getListForAdapter(list: MutableList<TransactionItem>?): List<ItemModel> {
        var result = mutableListOf<ItemModel>()
        list?.forEach {
            result.add(TransactionItemModel(it))
        }
        return result
    }

    private var onItemClick: (ItemModel, String) -> Unit = { item, action ->
        val transaction : TransactionItemModel = item as TransactionItemModel
        val currency = item.model.currency
        val amountOriginal = item.model.amount
        val amountEuro = item.model.amount
        when (action) {
            "no_action" -> {
                requireActivity().toastLong("Amount - $currency $amountOriginal | EUR $amountEuro")
            }
            else -> {
                requireActivity().toastLong("NO OTHER ACTION")
            }
        }
    }
}

