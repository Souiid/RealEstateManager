package com.openclassrooms.realestatemanager.ui.composable

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.openclassrooms.realestatemanager.R

@Composable
fun CheckAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.check_animation))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.size(250.dp)
    )
}