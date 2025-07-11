package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.features.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.features.screens.main.home.RealitiesViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class RealitiesViewModelTest {

    private val realtyRepository = mockk<IRealtyRepository>(relaxed = true)
    private val currencyViewModel = mockk<CurrencyViewModel>()
    private val context = mockk<Context>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RealitiesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Places::class)
        every { Places.initialize(any(), any()) } just Runs
        every { Places.createClient(any()) } returns mockk()

        every { currencyViewModel.isEuroFlow } returns MutableStateFlow(true)
        coEvery { realtyRepository.getAllRealties() } returns flowOf(emptyList())

        viewModel = RealitiesViewModel(realtyRepository, currencyViewModel, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }


    @Test
    fun `setCriteria updates criteriaFlow`() = runTest {
        //Arrange
        val criteria = SearchCriteria()

        //Act
        viewModel.setCriteria(criteria)
        val realties = mutableListOf<List<Realty>>()
        val job = launch {
            viewModel.realties.collect {
                realties.add(it)
            }
        }

        //Assert
        testDispatcher.scheduler.advanceUntilIdle()

        job.cancel()
    }
}