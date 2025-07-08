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
        Assert.assertEquals(115, utils.convertEuroToDollar(100))
    }

    @Test
    fun `convertDollarToEuro returns correct int`() {
        Assert.assertEquals(87, utils.convertDollarToEuro(100))
    }

    @Test
    fun `convertEuroToDollarDouble returns rounded double`() {
        val result = utils.convertEuroToDollarDouble(100.0)
        Assert.assertEquals(115.0, result, 0.001)
    }

    @Test
    fun `getCorrectPriceComponent handles euro to dollar logic`() {
        val result = utils.getCorrectPriceComponent(100, isEuro = false, isSaveInDollar = false)
        Assert.assertEquals(115, result.price)
        Assert.assertEquals("$", result.currency)
    }

    @Test
    fun `getCorrectPriceComponent handles dollar to euro logic`() {
        val result = utils.getCorrectPriceComponent(100, isEuro = true, isSaveInDollar = true)
        Assert.assertEquals(87, result.price)
        Assert.assertEquals("€", result.currency)
    }

    @Test
    fun `getTodayDate returns correctly formatted date`() {
        val calendar = Calendar.getInstance()
        calendar.set(2022, Calendar.JANUARY, 1)
        val result = utils.getTodayDate(calendar.time)
        Assert.assertEquals("01/01/2022", result)
    }

    @Test
    fun `filterOnlyDigits removes non-digit characters`() {
        Assert.assertEquals("12345", utils.filterOnlyDigits("a1b2c3d4e5"))
    }

    @Test
    fun `filterNumericInput handles commas and dots`() {
        Assert.assertEquals("123.45", utils.filterNumericInput("123,45"))
    }

    @Test
    fun `calculateDistanceInKm returns correct distance`() {
        val paris = LatLng(48.8566, 2.3522)
        val london = LatLng(51.5074, -0.1278)
        val distance = utils.calculateDistanceInKm(paris, london)
        Assert.assertTrue(distance in 340.0..350.0)
    }
}