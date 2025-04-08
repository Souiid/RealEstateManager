package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.repositories.AgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.NewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.RealtyRepository
import com.openclassrooms.realestatemanager.ui.MainViewModel
import com.openclassrooms.realestatemanager.ui.RealtyFormViewModel
import com.openclassrooms.realestatemanager.ui.SelectAgentViewModel
import com.openclassrooms.realestatemanager.ui.SetRealtyPictureViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RealtyFormViewModel(get()) }
    viewModel { SetRealtyPictureViewModel(get()) }
    viewModel { SelectAgentViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get()) }

    single<INewRealtyRepository> { NewRealtyRepository(androidContext()) }
    single<IAgentRepository> { AgentRepository(androidContext()) }
    single<IRealtyRepository> { RealtyRepository(androidContext()) }

}