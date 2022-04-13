package com.binding.mvvm.ui.binding

import androidx.databinding.BindingAdapter
import com.binding.mvvm.state.ViewState
import com.drake.statelayout.StateLayout


@BindingAdapter(value = ["bindViewState"], requireAll = false)
fun StateLayout.bindState(state: ViewState?) {
    state?.let {
        when (it) {
            is ViewState.Error -> showError()
            is ViewState.Content -> showContent()
            is ViewState.Empty -> showEmpty()
            is ViewState.Loading -> showLoading()
        }
    }
}