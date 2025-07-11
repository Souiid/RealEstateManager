package com.openclassrooms.realestatemanager.viewmodel

import android.content.res.Resources
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.features.screens.form.realtyform.RealtyFormViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class RealtyFormViewModelTest {

    private lateinit var viewModel: RealtyFormViewModel
    private val newRealtyRepository = mockk<INewRealtyRepository>(relaxed = true)
    private val realtyRepository = mockk<IRealtyRepository>(relaxed = true)
    private val utils = Utils()

    @Before
    fun setup() {
        viewModel = RealtyFormViewModel(newRealtyRepository, realtyRepository)
    }

    @Test
    fun `setPrimaryInfo sets newRealtyRepository when updatedRealty is null`() {
        // Arrange
        val primaryInfo = utils.createPrimaryInfo()

        // Act
        viewModel.setPrimaryInfo(primaryInfo)

        // Assert
        verify { newRealtyRepository.realtyPrimaryInfo = primaryInfo }
        verify(exactly = 0) { realtyRepository.updatedRealty = any() }
    }

    @Test
    fun `setPrimaryInfo updates updatedRealty when provided`() {
        // Arrange
        val primaryInfo = utils.createPrimaryInfo()
        val updatedRealty = utils.createRealty()

        // Act
        viewModel.setPrimaryInfo(primaryInfo, updatedRealty)

        // Assert
        assertSame(primaryInfo, updatedRealty.primaryInfo)
        verify { realtyRepository.updatedRealty = updatedRealty }
    }

    @Test
    fun `getFormValidationError returns error for missing surface`() {
        // Arrange
        val resources = mockk<Resources> {
            every { getString(R.string.error_surface_required) } returns "Surface is required"
        }

        // Act
        val error = viewModel.getFormValidationError(
            surface = "",
            price = "100000",
            rooms = "3",
            bedrooms = "2",
            bathrooms = "1",
            description = "desc",
            realtyPlace = RealtyPlace("1", "Place", LatLng(0.0, 0.0)),
            resources = resources
        )

        // Assert
        assertEquals("Surface is required", error)
    }

    @Test
    fun `getPrimaryInfo returns repository value`() {
        // Arrange
        val primaryInfo = utils.createPrimaryInfo()
        every { newRealtyRepository.realtyPrimaryInfo } returns primaryInfo

        // Act
        val result = viewModel.getPrimaryInfo()

        // Assert
        assertEquals(primaryInfo, result)
    }

    @Test
    fun `getRealtyFromRealtyRepository returns updatedRealty if present`() {
        // Arrange
        val realty = utils.createRealty()
        every { realtyRepository.updatedRealty } returns realty

        // Act
        val result = viewModel.getRealtyFromRealtyRepository()

        // Assert
        assertEquals(realty, result)
    }

    @Test
    fun `getRealtyFromRealtyRepository returns selectedRealtyFlow if updatedRealty is null`() {
        // Arrange
        val realty = utils.createRealty()
        every { realtyRepository.updatedRealty } returns null
        every { realtyRepository.selectedRealtyFlow } returns MutableStateFlow(realty)

        // Act
        val result = viewModel.getRealtyFromRealtyRepository()

        // Assert
        assertEquals(realty, result)
    }
}
