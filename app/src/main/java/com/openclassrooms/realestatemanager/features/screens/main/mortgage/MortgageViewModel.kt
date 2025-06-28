package com.openclassrooms.realestatemanager.features.screens.main.mortgage

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.MortgageResult
import com.openclassrooms.realestatemanager.data.Utils
import kotlin.math.pow

class MortgageViewModel: ViewModel() {

    fun calculateMortgage(
        propertyPrice: Double,
        downPayment: Double,
        interestRate: Double,
        durationYears: Int,
        isEuro: Boolean
    ): MortgageResult {
        val loanAmount = propertyPrice - downPayment
        val months = durationYears * 12
        val monthlyRate = interestRate / 12.0 / 100.0

        if (loanAmount <= 0.0 || months <= 0) {
            return MortgageResult(0.0, 0.0)
        }

        val denominator = 1 - (1 + monthlyRate).pow(-months.toDouble())
        val monthlyPayment = if (monthlyRate > 0.0 && denominator != 0.0) {
            loanAmount * monthlyRate / denominator
        } else {
            loanAmount / months.toDouble()
        }

        val totalCost = (monthlyPayment * months) - loanAmount

        return MortgageResult(
            monthlyPayment = if (isEuro) monthlyPayment else Utils().convertEuroToDollarDouble(monthlyPayment),
            totalCost =  if (isEuro) totalCost else Utils().convertEuroToDollarDouble(totalCost)
        )
    }

    fun isInputValid(
        downPayment: String,
        interestRate: String,
        durationYears: String
    ): Boolean {
        fun isValidDecimal(input: String): Boolean {
            if (input.isBlank()) return false
            val cleaned = input.replace(',', '.')
            val tooManyDots = cleaned.count { it == '.' } > 1
            return !tooManyDots && cleaned.toDoubleOrNull() != null
        }

        val interest = interestRate.replace(',', '.').toDoubleOrNull()
        val duration = durationYears.toIntOrNull()

        return isValidDecimal(downPayment) &&
                isValidDecimal(interestRate) &&
                interest != null && interest > 0.0 &&
                duration != null && duration >= 1
    }
}