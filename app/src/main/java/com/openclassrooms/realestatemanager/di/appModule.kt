package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.data.repositories.AgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.ISearchRepository
import com.openclassrooms.realestatemanager.data.repositories.NewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.RealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.SearchRepository
import com.openclassrooms.realestatemanager.ui.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.ui.screens.main.home.RealitiesViewModel
import com.openclassrooms.realestatemanager.ui.screens.main.descrpition.RealtyDescriptionViewModel
import com.openclassrooms.realestatemanager.ui.screens.form.realtyform.RealtyFormViewModel
import com.openclassrooms.realestatemanager.ui.screens.form.selectagent.SelectAgentViewModel
import com.openclassrooms.realestatemanager.ui.screens.form.setrealtypicture.SetRealtyPictureViewModel
import com.openclassrooms.realestatemanager.ui.screens.main.mortgage.MortgageViewModel
import com.openclassrooms.realestatemanager.ui.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { RealtyFormViewModel(get(), get()) }
    viewModel { SetRealtyPictureViewModel(get(), get()) }
    viewModel { SelectAgentViewModel(get(), get(), get()) }
    viewModel { RealitiesViewModel(get(), androidContext()) }
    viewModel { RealtyDescriptionViewModel(androidContext(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { MortgageViewModel() }
    viewModel { CurrencyViewModel(androidContext()) }

    single<INewRealtyRepository> { NewRealtyRepository(androidContext()) }
    single<IAgentRepository> { AgentRepository(androidContext()) }
    single<IRealtyRepository> { RealtyRepository(androidContext()) }
    single<ISearchRepository> { SearchRepository() }
}