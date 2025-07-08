package com.openclassrooms.realestatemanager.repository

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.RealtyPlace
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.RealtyType
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.RealtyRepository
import com.openclassrooms.realestatemanager.data.room.Amenity
import com.openclassrooms.realestatemanager.data.room.AppDatabase
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.dao.RealtyDao
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.*

class RealtyRepositoryTest {

    private lateinit var repository: RealtyRepository
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockDb = mockk<AppDatabase>(relaxed = true)
    private val mockDao = mockk<RealtyDao>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(DatabaseProvider)
        every { DatabaseProvider.getDatabase(mockContext) } returns mockDb
        every { mockDb.realtyDao() } returns mockDao

        repository = RealtyRepository(mockContext)
    }

    @Test
    fun `insertRealty calls dao insertRealty`() = runTest {
        val realty = mockk<Realty>()
        repository.insertRealty(realty)

        coVerify { mockDao.insertRealty(realty) }
    }

    @Test
    fun `getAllRealties returns dao flow`() {
        val expectedFlow = flowOf(emptyList<Realty>())
        every { mockDao.getAllRealties() } returns expectedFlow

        val result = repository.getAllRealties()

        assert(result === expectedFlow)
        verify { mockDao.getAllRealties() }
    }

    @Test
    fun `getRealtyFromID calls dao getRealtyById`() = runTest {
        val realty = mockk<Realty>()
        coEvery { mockDao.getRealtyById("1") } returns realty

        val result = repository.getRealtyFromID(1)

        assert(result == realty)
        coVerify { mockDao.getRealtyById("1") }
    }

    @Test
    fun `updateRealty calls dao updateRealty`() = runTest {
        val realty = mockk<Realty>()
        repository.updateRealty(realty)

        coVerify { mockDao.updateRealty(realty) }
    }

    @Test
    fun `getFilteredRealtiesFlow filters realties by minPrice and currency`() = runTest {
        // Arrange
        val realtyPlace = RealtyPlace("1", "TestPlace", LatLng(0.0, 0.0))
        val primaryInfo = RealtyPrimaryInfo(
            realtyType = RealtyType.HOUSE,
            surface = 120,
            price = 150000,
            roomsNbr = 5,
            bathroomsNbr = 2,
            bedroomsNbr = 3,
            description = "Nice house",
            realtyPlace = realtyPlace,
            amenities = listOf(Amenity.SCHOOL, Amenity.PARK),
            isEuro = true
        )
        val realty = Realty(
            id = 1,
            agentId = 1,
            entryDate = Date(),
            isAvailable = true,
            primaryInfo = primaryInfo,
            pictures = emptyList()
        )

        val criteria = SearchCriteria(
            minPrice = 100000,
            maxPrice = null,
            minSurface = null,
            maxSurface = null,
            minRooms = null,
            maxRooms = null,
            isAvailable = null,
            minEntryDate = null,
            maxEntryDate = null,
            minSoldDate = null,
            maxSoldDate = null,
            amenities = emptyList(),
            realtyTypes = emptyList(),
            selectedAgent = null,
            centerPlace = null,
            radiusKm = null
        )

        every {
            mockDao.getFilteredRealties(
                isAvailable = any(),
                minPrice = any(),
                maxPrice = any(),
                minSurface = any(),
                maxSurface = any(),
                minRooms = any(),
                maxRooms = any(),
                minEntryDate = any(),
                maxEntryDate = any(),
                minSoldDate = any(),
                maxSoldDate = any(),
                realtyTypes = any(),
                realtyTypesSize = any(),
                agentId = any()
            )
        } returns listOf(realty)

        // Act
        val flow = repository.getFilteredRealtiesFlow(criteria = criteria, isEuro = true)

        // Assert
        flow.collect { result ->
            assert(result.size == 1)
            assert(result[0] == realty)
        }

        verify {
            mockDao.getFilteredRealties(
                isAvailable = null,
                minPrice = null,
                maxPrice = null,
                minSurface = null,
                maxSurface = null,
                minRooms = null,
                maxRooms = null,
                minEntryDate = null,
                maxEntryDate = null,
                minSoldDate = null,
                maxSoldDate = null,
                realtyTypes = match { it == null || it.isEmpty() },
                realtyTypesSize = 0,
                agentId = null
            )
        }
    }
}