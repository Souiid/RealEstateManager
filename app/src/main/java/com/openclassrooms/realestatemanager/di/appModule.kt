package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.NewRealtyRepository
import com.openclassrooms.realestatemanager.ui.RealtyFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RealtyFormViewModel(get()) }
    single<INewRealtyRepository> { NewRealtyRepository() }

}