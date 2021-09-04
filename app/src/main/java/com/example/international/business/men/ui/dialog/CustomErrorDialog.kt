package com.example.international.business.men.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.international.business.men.R
import com.example.international.business.men.databinding.DialogCustomErrorBinding
import com.example.international.business.men.utils.setOneOffClickListener

class CustomErrorDialog(
    val title: String? = null,
    val message: String? = null,
    val buttonText: String? = null,
    private val onClick: () -> Unit = { }
): DialogFragment() {
    private var mView: View? = null
    private var _binding: DialogCustomErrorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // either this way we can init dialogBinding
        _binding = DialogCustomErrorBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title?.let {
            binding.tvErrorTitle.text = it
        }
        message?.let {
            binding.tvErrorDescription.text = it
        }
        buttonText?.let {
            binding.btContinue.text = it
        }

        binding.btContinue.setOneOffClickListener {
            onClick.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}