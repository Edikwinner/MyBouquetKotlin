package com.example.mybouquetkotlin.DI

import com.example.mybouquetkotlin.Model.Repository.CardRepository
import com.example.mybouquetkotlin.ViewModel.Fragments.AddViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.DescriptionViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.HomeViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.LoginViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.ShoppingCardViewModel
import com.example.mybouquetkotlin.ViewModel.Fragments.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CardRepository> { CardRepository() }
    viewModel<AddViewModel> { AddViewModel(get()) }
    viewModel<DescriptionViewModel> { DescriptionViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<ShoppingCardViewModel> { ShoppingCardViewModel(get()) }
    viewModel<UserViewModel> { UserViewModel(get()) }

}