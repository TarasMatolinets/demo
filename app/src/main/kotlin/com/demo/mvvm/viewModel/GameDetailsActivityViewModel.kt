package com.demo.mvvm.viewModel


import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class GameDetailsActivityViewModel @Inject constructor() :  ViewModel() {
    var title: ObservableField<String> = ObservableField("")
    var rating: ObservableInt = ObservableInt(0)
}
