package com.binding.mvvm.ui.page.config

import android.util.SparseArray
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel

data class DataBindingConfig(
    @LayoutRes val layout: Int,
    val vmVariableId: Int = -1,
    val stateViewModel: ViewModel? = null
) {

    val bindingParams: SparseArray<Any> = SparseArray()

    fun addBindingParam(variableId: Int, any: Any): DataBindingConfig {
        if (bindingParams[variableId] == null) {
            bindingParams.put(variableId, any)
        }
        return this
    }
}
