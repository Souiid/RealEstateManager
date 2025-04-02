package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.NewRealtyRepository
import com.openclassrooms.realestatemanager.ui.RealtyFormViewModel
import com.openclassrooms.realestatemanager.ui.SelectAgentViewModel
import com.openclassrooms.realestatemanager.ui.SetRealtyPictureViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RealtyFormViewModel(get()) }
    viewModel { SetRealtyPictureViewModel(get()) }
    viewModel { SelectAgentViewModel(get()) }

    single<INewRealtyRepository> { NewRealtyRepository(androidContext()) }
}