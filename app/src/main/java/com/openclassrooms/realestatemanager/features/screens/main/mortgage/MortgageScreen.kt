package com.openclassrooms.realestatemanager.features.screens.main.mortgage

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.MortgageResult
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.formatSmart
import com.openclassrooms.realestatemanager.features.screens.CurrencyViewModel
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MortgageScreen(
    mortgageViewModel: MortgageViewModel = koinViewModel(),
    currencyViewModel: CurrencyViewModel = koinViewModel(),
    price: Int
) {
    val utils = Utils()
    var downPayment by remember { mutableStateOf("0.0") }
    var interestRate by remember { mutableStateOf("1.0") }
    var durationYears by remember { mutableStateOf("10") }
    var mortgageResult by remember { mutableStateOf<MortgageResult?>(null) }
    val isEuro by currencyViewModel.isEuroFlow.collectAsState()
    val priceComponent = utils.getCorrectPriceComponent(price, isEuro)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.Start
    ) {


        ThemeText(
            text = stringResource(R.string.the_pice_of_this_realty, priceComponent.price.toString(), priceComponent.currency),
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
                downPayment = utils.filterNumericInput(value)
            },
            value = downPayment,
            labelID = R.string.down_payment,
            imeAction = ImeAction.Next,
            iconText = priceComponent.currency,
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(5.dp))

        ThemeOutlinedTextField(
            onValueChanged = { value ->
                interestRate = utils.filterNumericInput(value)
            },
            value = interestRate,
            labelID = R.string.interest_rate,
            imeAction = ImeAction.Next,
            iconText = "%",
            keyboardType = KeyboardType.Decimal,
        )
        Spacer(modifier = Modifier.height(5.dp))

        ThemeOutlinedTextField(
            onValueChanged = { value ->
                durationYears = utils.filterNumericInput(value, allowDecimal = false)
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

                if (!mortgageViewModel.isInputValid(downPayment, interestRate, durationYears)) {
                    Toast.makeText(context, context.getString(R.string.one_field_empty), Toast.LENGTH_SHORT).show()
                    return@ThemeButton
                }

                mortgageResult = mortgageViewModel.calculateMortgage(
                    propertyPrice = priceComponent.price.toDouble(),
                    downPayment = downPayment.toDouble(),
                    interestRate = interestRate.toDouble(),
                    durationYears = durationYears.toInt(),
                    isEuro = isEuro
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

            MortgageResultSection(it, isEuro)
        }
    }
}

@Composable
fun MortgageResultSection(mortgageResult: MortgageResult, isEuro: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        MortgageResultRow(
            stringResource(R.string.monthly_payment),
            mortgageResult.monthlyPayment.formatSmart(),
            isEuro
        )
        MortgageResultRow(
            stringResource(R.string.total_cost),
            mortgageResult.totalCost.formatSmart(),
            isEuro
        )
    }

}

@Composable
fun MortgageResultRow(
    titleText: String,
    resultString: String,
    isEuro: Boolean,
    utils: Utils = Utils()
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
            text = "$resultString ${utils.getCorrectPriceComponent(0, isEuro).currency}",
            style = ThemeTextStyle.NORMAL,
        )
    }
}