package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.openclassrooms.realestatemanager.data.DataStoreManager
import com.openclassrooms.realestatemanager.features.screens.CurrencyViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest {

    private val context: Context = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CurrencyViewModel

    private val euroFlow = MutableStateFlow(true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(DataStoreManager)

        every { DataStoreManager.getIsEuro(context) } returns euroFlow
        coEvery { DataStoreManager.setIsEuro(context, any()) } just Runs

        viewModel = CurrencyViewModel(context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `isEuroFlow emits true initially`() = runTest {
        // Act
        val value = viewModel.isEuroFlow.value

        // Assert
        assertEquals(true, value)
    }

    @Test
    fun `setIsEuro calls DataStoreManager with correct value`() = runTest {
        // Arrange
        viewModel.setIsEuro(false)

        // Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { DataStoreManager.setIsEuro(context, false) }
    }
}