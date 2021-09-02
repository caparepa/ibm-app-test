package com.example.international.business.men.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.databinding.FragmentTransactionListBinding
import com.example.international.business.men.ui.adapter.base.DynamicAdapter
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.TransactionItemModel
import com.example.international.business.men.ui.adapter.type.factory.TransactionItemTypeFactoryImpl
import com.example.international.business.men.ui.viewmodel.ProductTransactionViewModel
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

    private var binding : FragmentTransactionListBinding? = null
    private var sku: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sku = it.getString("sku").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //observeViewModel()
        binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        if(sku != null)
            requireActivity().toastLong("NO NULL! $sku")
        else
            requireActivity().toastLong("SKU NULL!")

        //loadTransactionList(sku)
    }

    private fun loadTransactionList(sku: String) {

    }

    private fun observeViewModel() = productTransactionViewModel.run {

    }

    private fun setUpTransactionListAdapter(list: List<TransactionItem>) {

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
                requireActivity().toastLong("Amount - $currency $amountOriginal / EUR $amountEuro")
            }
            else -> {
                requireActivity().toastLong("NO ACTION")
            }
        }
    }
}

