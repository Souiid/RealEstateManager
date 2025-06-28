package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.repositories.AgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.ISearchRepository
import com.openclassrooms.realestatemanager.data.repositories.NewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.RealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.SearchRepository
import com.openclassrooms.realestatemanager.features.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.features.screens.form.realtyform.RealtyFormViewModel
import com.openclassrooms.realestatemanager.features.screens.form.selectagent.SelectAgentViewModel
import com.openclassrooms.realestatemanager.features.screens.form.setrealtypicture.SetRealtyPictureViewModel
import com.openclassrooms.realestatemanager.features.screens.main.descrpition.RealtyDescriptionViewModel
import com.openclassrooms.realestatemanager.features.screens.main.home.RealitiesViewModel
import com.openclassrooms.realestatemanager.features.screens.main.mortgage.MortgageViewModel
import com.openclassrooms.realestatemanager.features.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RealtyFormViewModel(get(), get()) }
    viewModel { SetRealtyPictureViewModel(get(), get()) }
    viewModel { SelectAgentViewModel(get(), get(), get()) }
    viewModel { RealitiesViewModel(get(), get(), androidContext()) }
    viewModel { RealtyDescriptionViewModel(androidContext(), get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { MortgageViewModel() }
    viewModel { CurrencyViewModel(androidContext()) }

    single<INewRealtyRepository> { NewRealtyRepository() }
    single<IAgentRepository> { AgentRepository(androidContext()) }
    single<IRealtyRepository> { RealtyRepository(androidContext()) }
    single<ISearchRepository> { SearchRepository() }
}