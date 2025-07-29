package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.data.Utils
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InternetInstrumentedTest {

    @Test
    fun internetIsAvailable_shouldEmitTrue() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val utils = Utils()

        utils.startInternetMonitoring(context)

        val isConnected = withTimeoutOrNull(5000L) {
            utils.internetStatus.first { it }
        }

        val expectedToastMessage = context.getString(R.string.internet_available)

        assertTrue(expectedToastMessage, isConnected == true)
    }

    // Requires Wi-Fi to be disabled manually before running
    @Test
    fun internetIsNotAvailable_shouldEmitFalse() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val utils = Utils()

        utils.startInternetMonitoring(context)

        kotlinx.coroutines.delay(500)

        val currentStatus = utils.internetStatus.value
        val expectedToastMessage = context.getString(R.string.not_internet)

        assertTrue(expectedToastMessage, !currentStatus)
    }
}