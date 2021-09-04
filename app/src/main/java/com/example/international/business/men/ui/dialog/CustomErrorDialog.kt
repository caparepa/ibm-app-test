package com.example.international.business.men.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.international.business.men.R
import com.example.international.business.men.databinding.DialogCustomErrorBinding
import com.example.international.business.men.utils.setOneOffClickListener

class CustomErrorDialog(
    context: Context,
    val title: String? = null,
    val message: String? = null,
    val buttonText: String? = null,
    private val onClick: () -> Unit = { }
) : Dialog(context) {

    private var _binding: DialogCustomErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogCustomErrorBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
    }

    private fun setDialog() {
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

}