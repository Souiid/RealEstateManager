package com.openclassrooms.realestatemanager.ui.screens.main

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.MortgageResult
import com.openclassrooms.realestatemanager.data.Utils

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

        val denominator = 1 - Math.pow(1 + monthlyRate, -months.toDouble())
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
}