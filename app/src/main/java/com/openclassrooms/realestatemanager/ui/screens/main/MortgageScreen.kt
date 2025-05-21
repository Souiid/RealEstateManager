package com.openclassrooms.realestatemanager.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.idrisssouissi.smartbait.presentation.components.ThemeText
import com.idrisssouissi.smartbait.presentation.components.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.MortgageResult
import com.openclassrooms.realestatemanager.data.formatSmart
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun MortgageScreen(
    mortgageViewModel: MortgageViewModel = koinViewModel(),
    price: Double
) {

    var downPayment by remember { mutableStateOf("0") }
    var interestRate by remember { mutableStateOf("1") }
    var durationYears by remember { mutableStateOf("10") }
    var mortgageResult by remember { mutableStateOf<MortgageResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {

        ThemeText(
            text = "$price €",
            style = ThemeTextStyle.TITLE,
        )

        Spacer(modifier = Modifier.height(20.dp))

        ThemeText(
            text = "Entrer",
            style = ThemeTextStyle.NORMAL,
        )
        Spacer(modifier = Modifier.height(20.dp))

        ThemeOutlinedTextField(
            onValueChanged = { value ->
                downPayment = value
            },
            value = downPayment,
            labelID = R.string.down_payment,
            imeAction = ImeAction.Next,
            iconText = "$",
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(5.dp))

        ThemeOutlinedTextField(
            onValueChanged = { value ->
                interestRate = value
            },
            value = interestRate,
            labelID = R.string.interest_rate,
            imeAction = ImeAction.Next,
            iconText = "%",
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(5.dp))

        ThemeOutlinedTextField(
            onValueChanged = { value ->
                durationYears = value
            },
            value = durationYears,
            labelID = R.string.duration_years,
            imeAction = ImeAction.Done,
            iconText = "years",
            keyboardType = KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(20.dp))

        ThemeButton(
            onClick = {
                mortgageResult = mortgageViewModel.calculateMortgage(
                    propertyPrice = price,
                    downPayment = downPayment.toDouble(),
                    interestRate = interestRate.toDouble(),
                    durationYears = durationYears.toInt()
                )
            },
            text = stringResource(R.string.calculate),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        mortgageResult?.let {
            ThemeText(
                text = stringResource(R.string.result),
                style = ThemeTextStyle.TITLE,
            )
            Spacer(modifier = Modifier.height(5.dp))

            MortgageResultSection(it)
        }
    }
}

@Composable
fun MortgageResultSection(mortgageResult: MortgageResult) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        MortgageResultRow(stringResource(R.string.monthly_payment), mortgageResult.monthlyPayment)
        MortgageResultRow(stringResource(R.string.total_cost), mortgageResult.totalCost)
    }

}

@Composable
fun MortgageResultRow(
    titleText: String,
    resultDouble: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThemeText(
            text = titleText,
            style = ThemeTextStyle.NORMAL,
        )

        ThemeText(
            text = resultDouble.formatSmart(),
            style = ThemeTextStyle.NORMAL,
        )
    }
}