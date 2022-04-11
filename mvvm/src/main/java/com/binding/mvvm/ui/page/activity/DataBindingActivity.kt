package com.binding.mvvm.ui.page.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.binding.mvvm.ui.page.config.DataBindingConfig

abstract class DataBindingActivity : AppCompatActivity() {

    private var mBinding: ViewDataBinding? = null

    protected fun getBinding() = mBinding

    abstract fun getDataBindingConfig(): DataBindingConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBindingConfig = getDataBindingConfig()

        mBinding =
            DataBindingUtil.setContentView<ViewDataBinding>(this, dataBindingConfig.layout).apply {
                lifecycleOwner = this@DataBindingActivity
                setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)
                dataBindingConfig.bindingParams.forEach { key, value ->
                    setVariable(key, value)
                }
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }
}