package com.binding.mvvm.ui.page.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.binding.mvvm.ui.page.config.DataBindingConfig

abstract class DataBindingFragment : Fragment() {

    private var mBinding: ViewDataBinding? = null

    fun getBinding() = mBinding

    abstract fun getDataBindingConfig(): DataBindingConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dataBindingConfig = getDataBindingConfig()

        mBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            dataBindingConfig.layout,
            container,
            false
        ).apply {
            lifecycleOwner = this@DataBindingFragment
            setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)
            dataBindingConfig.bindingParams.forEach { key, value ->
                setVariable(key, value)
            }
        }
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }
}