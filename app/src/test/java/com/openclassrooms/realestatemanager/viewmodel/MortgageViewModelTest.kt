package com.openclassrooms.realestatemanager.viewmodel

import com.openclassrooms.realestatemanager.features.screens.main.mortgage.MortgageViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MortgageViewModelTest {

    private lateinit var viewModel: MortgageViewModel

    @Before
    fun setup() {
        viewModel = MortgageViewModel()
    }

    @Test
    fun `calculateMortgage returns correct result in euro`() {
        // Act
        val result = viewModel.calculateMortgage(
            propertyPrice = 200000.0,
            downPayment = 50000.0,
            interestRate = 2.0,
            durationYears = 20,
            isEuro = true
        )
        // Assert
        assertTrue(result.monthlyPayment > 0)
        assertTrue(result.totalCost > 0)
    }

    @Test
    fun `calculateMortgage returns 0 if loan amount is zero`() {
        // Act
        val result = viewModel.calculateMortgage(
            propertyPrice = 50000.0,
            downPayment = 50000.0,
            interestRate = 2.0,
            durationYears = 20,
            isEuro = true
        )
        // Assert
        assertEquals(0.0, result.monthlyPayment, 0.001)
        assertEquals(0.0, result.totalCost, 0.001)
    }

    @Test
    fun `calculateMortgage returns 0 if duration is zero`() {
        // Act
        val result = viewModel.calculateMortgage(
            propertyPrice = 200000.0,
            downPayment = 50000.0,
            interestRate = 2.0,
            durationYears = 0,
            isEuro = true
        )
        // Assert
        assertEquals(0.0, result.monthlyPayment, 0.001)
        assertEquals(0.0, result.totalCost, 0.001)
    }

    @Test
    fun `isInputValid returns false for blank inputs`() {
        // Act
        val result = viewModel.isInputValid("", "", "")
        // Assert
        assertFalse(result)
    }

    @Test
    fun `isInputValid returns false for negative or zero interest`() {
        // Act
        val result = viewModel.isInputValid("10000", "0", "20")
        // Assert
        assertFalse(result)
    }

    @Test
    fun `isInputValid returns false for duration less than 1`() {
        // Act
        val result = viewModel.isInputValid("10000", "1.5", "0")
        // Assert
        assertFalse(result)
    }

    @Test
    fun `isInputValid returns true for correct input`() {
        // Arrange
        // Act
        val result = viewModel.isInputValid("10000", "1.5", "20")
        // Assert
        assertTrue(result)
    }

    @Test
    fun `isInputValid handles comma as decimal separator`() {
        // Act
        val result = viewModel.isInputValid("10000", "1,5", "20")
        // Assert
        assertTrue(result)
    }

    @Test
    fun `calculateMortgage returns dollar values when isEuro is false`() {
        // Act
        val resultEuro = viewModel.calculateMortgage(200000.0, 50000.0, 2.0, 20, true)
        val resultDollar = viewModel.calculateMortgage(200000.0, 50000.0, 2.0, 20, false)
        // Assert
        assertNotEquals(resultEuro.monthlyPayment, resultDollar.monthlyPayment)
        assertNotEquals(resultEuro.totalCost, resultDollar.totalCost)
    }
}