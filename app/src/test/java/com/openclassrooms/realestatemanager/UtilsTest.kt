package com.openclassrooms.realestatemanager

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class UtilsTest {

    private lateinit var utils: Utils

    @Before
    fun setUp() {
        utils = Utils()
    }

    @Test
    fun `convertEuroToDollar returns correct int`() {
        // Act
        val result = utils.convertEuroToDollar(100)
        // Assert
        Assert.assertEquals(115, result)
    }

    @Test
    fun `convertDollarToEuro returns correct int`() {
        // Act
        val result = utils.convertDollarToEuro(100)
        // Assert
        Assert.assertEquals(87, result)
    }

    @Test
    fun `convertEuroToDollarDouble returns rounded double`() {
        // Act
        val result = utils.convertEuroToDollarDouble(100.0)
        // Assert
        Assert.assertEquals(115.0, result, 0.001)
    }

    @Test
    fun `getCorrectPriceComponent handles euro to dollar logic`() {
        // Act
        val result = utils.getCorrectPriceComponent(100, isEuro = false, isSaveInDollar = false)
        // Assert
        Assert.assertEquals(115, result.price)
        Assert.assertEquals("$", result.currency)
    }

    @Test
    fun `getCorrectPriceComponent handles dollar to euro logic`() {
        // Act
        val result = utils.getCorrectPriceComponent(100, isEuro = true, isSaveInDollar = true)
        // Assert
        Assert.assertEquals(87, result.price)
        Assert.assertEquals("€", result.currency)
    }

    @Test
    fun `getTodayDate returns correctly formatted date`() {
        // Arrange
        val calendar = Calendar.getInstance()
        calendar.set(2022, Calendar.JANUARY, 1)
        // Act
        val result = utils.getTodayDate(calendar.time)
        // Assert
        Assert.assertEquals("01/01/2022", result)
    }

    @Test
    fun `filterOnlyDigits removes non-digit characters`() {
        // Act
        val result = utils.filterOnlyDigits("a1b2c3d4e5")
        // Assert
        Assert.assertEquals("12345", result)
    }

    @Test
    fun `filterNumericInput handles commas and dots`() {
        // Act
        val result = utils.filterNumericInput("123,45")
        // Assert
        Assert.assertEquals("123.45", result)
    }

    @Test
    fun `calculateDistanceInKm returns correct distance`() {
        // Arrange
        val paris = LatLng(48.8566, 2.3522)
        val london = LatLng(51.5074, -0.1278)
        // Act
        val distance = utils.calculateDistanceInKm(paris, london)
        // Assert
        Assert.assertTrue(distance in 340.0..350.0)
    }
}