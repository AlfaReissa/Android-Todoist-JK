package com.readthym.doesapp.di

import com.readthym.doesapp.ui.auth.AuthViewModel
import com.readthym.doesapp.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

