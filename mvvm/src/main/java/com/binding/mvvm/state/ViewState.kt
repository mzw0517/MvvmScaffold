package com.binding.mvvm.state

sealed class ViewState{
    object Loading: ViewState()
    object Empty: ViewState()
    object Error: ViewState()
    object Content: ViewState()
}
