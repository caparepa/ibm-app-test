package com.example.international.business.men.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.databinding.FragmentTransactionListBinding
import com.example.international.business.men.ui.adapter.base.DynamicAdapter
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.ExtendedTransactionItemModel
import com.example.international.business.men.ui.adapter.type.factory.TransactionItemTypeFactoryImpl
import com.example.international.business.men.ui.dialog.CustomErrorDialog
import com.example.international.business.men.ui.viewmodel.ProductTransactionViewModel
import com.example.international.business.men.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionListFragment : Fragment(), KoinComponent {

    private val productTransactionViewModel: ProductTransactionViewModel by viewModel()

    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!

    private var sku: String? = null
    private var currencySet: List<String>? = null

    private var targetRatesList: List<ExchangeRateItem>? = null       //list with the target currency exchange rate
    private var allTransactionList: List<TransactionItem>? = null   //all transaction list from API
    private var trimTransactionList: List<TransactionItem>? = null  //filtered transaction list (discarding currencies not found in [currencySet]
    private var skuTransactionList: List<TransactionItem>? = null   //filtered transaction list by sku

    private var transactionAdapter: DynamicAdapter? = null
    private var warningDialog: CustomErrorDialog? = null

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
        observeViewModel()
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        warningDialog = CustomErrorDialog(requireActivity(), onClick = { })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sku?.let {
            loadData()
            binding.tvSkuValue.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadData() {
        productTransactionViewModel.getExchangeRateList()
    }

    private fun observeViewModel() = productTransactionViewModel.run {
        exchangeRateList.observe(viewLifecycleOwner, Observer {
            it?.let {
                //obtain currency set from current exchange rate pool
                currencySet = it.getCurrencySet().toList()
                //get the currency exchange rate that are missing for the target currency (e.g. EUR)
                getMissingCurrencyRates(CURRENCY_EUR, it)
            }
        })
        filteredRateList.observe(viewLifecycleOwner, Observer {
            it?.let {
                //get target rate list
                targetRatesList = it
                //filter the the transactions that don't cover the currency set
                trimTransactionList = allTransactionList!!.filterMissingCurrencyTransactions(it)
                //filter all transactions by sku
                getTransactionsBySku(sku!!, allTransactionList!!)
            }
        })
        transactionBySkuList.observe(viewLifecycleOwner, Observer {
            it?.let {
                //
                skuTransactionList = it
                //obtain the sum for the trimmed filtered transactions
                getTransactionSumInCurrency(targetRatesList!!, trimTransactionList!!)
            }
        })
        totalTransactionSumInCurrency.observe(viewLifecycleOwner, Observer {
            it?.let {
                //set up total amount
                val totalSum = "$CURRENCY_EUR $it"
                binding.tvTotalAmountValue.text = totalSum

                Log.d("TAGTAG", "${allTransactionList!!.size} != ${trimTransactionList!!.size}")
                if(allTransactionList!!.size != trimTransactionList!!.size) {
                    showWarningDialog()
                }

                //set up the adapter with the filtered original transaction list
                getExtendedTransactionList(CURRENCY_EUR, targetRatesList!!, skuTransactionList!!)
            }
        })
        extendedTransactionList.observe(viewLifecycleOwner, Observer {
            it?.let {
                setUpTransactionListAdapter(it)
            }
        })
    }

    private fun setUpTransactionListAdapter(list: List<ExtendedTransactionItem>) {
        transactionAdapter = getTransactionListAdapter(list)
        binding.rvTransactionList.adapter = transactionAdapter
        binding.rvTransactionList.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getTransactionListAdapter(list: List<ExtendedTransactionItem>): DynamicAdapter {
        return DynamicAdapter(
            typeFactory = TransactionItemTypeFactoryImpl(),
            items = getListForAdapter(list.toMutableList()),
            onClick = { _, _ -> }
        )
    }

    private fun getListForAdapter(list: MutableList<ExtendedTransactionItem>?): List<ItemModel> {
        var result = mutableListOf<ItemModel>()
        list?.forEach {
            result.add(ExtendedTransactionItemModel(it))
        }
        return result
    }

    private fun showWarningDialog() {
        warningDialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCancelable(true)
            it.show()
        }
    }
}

