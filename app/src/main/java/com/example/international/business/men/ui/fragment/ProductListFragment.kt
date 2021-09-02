package com.example.international.business.men.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.databinding.FragmentProductListBinding
import com.example.international.business.men.ui.adapter.base.DynamicAdapter
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.ProductItemModel
import com.example.international.business.men.ui.adapter.type.factory.TransactionItemTypeFactoryImpl
import com.example.international.business.men.ui.viewmodel.ProductTransactionViewModel
import com.example.international.business.men.utils.toastLong
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [ProductListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductListFragment : Fragment() {

    private val productTransactionViewModel: ProductTransactionViewModel by sharedViewModel()

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private var productAdapter: DynamicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        observeViewModel()
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadProductList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadProductList() {
        productTransactionViewModel.getExchangeRateList()
        productTransactionViewModel.getTransactionList()
    }

    private fun observeViewModel() = productTransactionViewModel.run {
        loadingState.observe(viewLifecycleOwner, Observer {

        })
        productList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                requireActivity().toastLong("PROD - HAY DATA!")
                setUpProductListAdapter(it)
            } else
                requireActivity().toastLong("PROD - NO HAY DATA!")
        })
        exchangeRateList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty())
                requireActivity().toastLong("EX - HAY DATA!")
            else
                requireActivity().toastLong("EX - NO HAY DATA!")
        })
        transactionList.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty())
                productTransactionViewModel.getProductList(it)
            else
                requireActivity().toastLong("TX - NO HAY DATA!")
        })
        onError.observe(viewLifecycleOwner, Observer {
            requireActivity().toastLong(it)
        })
    }

    private fun setUpProductListAdapter(list: List<TransactionItem>) {
        productAdapter = getProductListAdapter(list)
        binding.rvProductList.adapter = productAdapter
        binding.rvProductList.layoutManager = LinearLayoutManager(requireActivity())

    }

    private fun getProductListAdapter(list: List<TransactionItem>): DynamicAdapter {
        return DynamicAdapter(
            typeFactory = TransactionItemTypeFactoryImpl(),
            items = getListForAdapter(list.toMutableList()),
            onClick = onItemClick
        )
    }

    private fun getListForAdapter(list: MutableList<TransactionItem>?): List<ItemModel> {
        var result = mutableListOf<ItemModel>()
        list?.forEach {
            result.add(ProductItemModel(it))
        }
        return result
    }

    private var onItemClick: (ItemModel, String) -> Unit = { item, action ->
        val product: ProductItemModel = item as ProductItemModel
        val sku = product.model.sku
        when(action) {
            "show_toast" -> {
                requireActivity().toastLong("You selected the product: $sku")
            }
            "go_to_detail" -> {
                requireActivity().toastLong("You're opening $sku detail")
                val action: NavDirections = ProductListFragmentDirections.actionProductListFragmentToTransactionListFragment()
                findNavController().navigate(action)
            }
            else -> {
                Log.d("Debug@ProductListFrament", "Other action")
            }
        }
    }

}