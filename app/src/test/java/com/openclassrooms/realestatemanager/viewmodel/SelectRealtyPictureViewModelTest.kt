package com.openclassrooms.realestatemanager.viewmodel

import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.features.screens.form.setrealtypicture.SetRealtyPictureViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetRealtyPictureViewModelTest {

    private lateinit var viewModel: SetRealtyPictureViewModel
    private val newRealtyRepository = mockk<INewRealtyRepository>(relaxed = true)
    private val realtyRepository = mockk<IRealtyRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val utils = Utils()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SetRealtyPictureViewModel(newRealtyRepository, realtyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setRealtyPictures sets images when updatedRealty is null`() {
        // Arrange
        val pictures = listOf(mockk<RealtyPicture>())
        var completionCalled = false

        // Act
        viewModel.setRealtyPictures(pictures, null) {
            completionCalled = true
        }

        // Assert
        verify { newRealtyRepository.images = pictures }
        assert(completionCalled)
    }

    @Test
    fun `setRealtyPictures updates realty and resets repos when updatedRealty is not null`() = runTest {
        // Arrange
        val pictures = listOf(mockk<RealtyPicture>())
        val realty = utils.createRealty()
        var completionCalled = false

        // Act
        viewModel.setRealtyPictures(pictures, realty) {
            completionCalled = true
        }

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify {
            realtyRepository.setSelectedRealty(match { it.pictures == pictures && it.id == realty.id })
            realtyRepository.updateRealty(match { it.pictures == pictures && it.id == realty.id })
            realtyRepository.updatedRealty = null
            newRealtyRepository.images = null
            newRealtyRepository.realtyPrimaryInfo = null
        }
        // Assert
        assert(completionCalled)
    }

    @Test
    fun `getUpdatedRealty returns value from repository`() {
        // Arrange
        val realty = utils.createRealty()
        every { realtyRepository.updatedRealty } returns realty

        // Act
        val result = viewModel.getUpdatedRealty()

        // Assert
        assertEquals(realty, result)
    }

    @Test
    fun `getRealtyPictures returns value from repository`() {
        // Arrange
        val pictures = listOf(mockk<RealtyPicture>())
        every { newRealtyRepository.images } returns pictures

        // Act
        val result = viewModel.getRealtyPictures()

        // Assert
        assertEquals(pictures, result)
    }
}