package com.example.international.business.men.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.international.business.men.R
import com.example.international.business.men.databinding.FragmentTransactionListBinding
import com.example.international.business.men.ui.viewmodel.ProductTransactionViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.component.KoinComponent

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionListFragment : Fragment(), KoinComponent {

    private val productTransactionViewModel: ProductTransactionViewModel by sharedViewModel()

    private var binding : FragmentTransactionListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        //runViewModel()
    }
}