package com.example.international.business.men.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.international.business.men.databinding.FragmentProductListBinding
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
            if(!it.isNullOrEmpty())
                requireActivity().toastLong("PROD - HAY DATA!")
            else
                requireActivity().toastLong("PROD - NO HAY DATA!")
        })
        exchangeRateList.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty())
                requireActivity().toastLong("EX - HAY DATA!")
            else
                requireActivity().toastLong("EX - NO HAY DATA!")
        })
        transactionList.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty())
                productTransactionViewModel.getProductList(it)
            else
                requireActivity().toastLong("TX - NO HAY DATA!")
        })
        onError.observe(viewLifecycleOwner, Observer {
            requireActivity().toastLong(it)
        })
    }

    private fun setUpProductListAdapter() {

    }

}