package com.openclassrooms.realestatemanager.repository

import android.content.Context
import com.openclassrooms.realestatemanager.data.repositories.RealtyRepository
import com.openclassrooms.realestatemanager.data.room.AppDatabase
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.dao.RealtyDao
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.viewmodel.Utils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

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
        // Arrange
        val realty = mockk<Realty>()

        // Act
        repository.insertRealty(realty)

        // Assert
        coVerify { mockDao.insertRealty(realty) }
    }

    @Test
    fun `getAllRealties returns dao flow`() {
        // Arrange
        val expectedFlow = flowOf(emptyList<Realty>())
        every { mockDao.getAllRealties() } returns expectedFlow

        // Act
        val result = repository.getAllRealties()

        // Assert
        assert(result === expectedFlow)
        verify { mockDao.getAllRealties() }
    }

    @Test
    fun `getRealtyFromID calls dao getRealtyById`() = runTest {
        // Arrange
        val realty = mockk<Realty>()
        coEvery { mockDao.getRealtyById("1") } returns realty

        // Act
        val result = repository.getRealtyFromID(1)

        // Assert
        assert(result == realty)
        coVerify { mockDao.getRealtyById("1") }
    }

    @Test
    fun `updateRealty calls dao updateRealty`() = runTest {
        // Arrange
        val realty = mockk<Realty>()
        // Act
        repository.updateRealty(realty)

        // Assert
        coVerify { mockDao.updateRealty(realty) }
    }

    @Test
    fun `getFilteredRealtiesFlow filters by minPrice with currency conversion`() = runTest {
        val utils = Utils()
        val highRealty = utils.createRealtyWithPrice(id = 1, price = 180000, isEuro = true)
        val lowRealty = utils.createRealtyWithPrice(id = 2, price = 100000, isEuro = true)
        val criteria = utils.createSearchCriteria(minPrice = 150000)

        coEvery {
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
                realtyTypes = match { it.isEmpty() },
                realtyTypesSize = 0,
                agentId = null
            )
        } returns utils.createFilteredRealties(highRealty, lowRealty)

        val result = repository.getFilteredRealtiesFlow(criteria, isEuro = true).toList().flatten()

        assert(result.size == 1)
        assert(result[0].id == highRealty.id)
    }

    @Test
    fun `getFilteredRealtiesFlow filters by minSurface`() = runTest {
        val utils = Utils()
        val realty = utils.createRealty().copy(id = 1, primaryInfo = utils.createPrimaryInfo().copy(surface = 120))
        val criteria = utils.createSearchCriteria().copy(minSurface = 100)

        coEvery {
            mockDao.getFilteredRealties(
                isAvailable = null,
                minPrice = null,
                maxPrice = null,
                minSurface = 100.0,
                maxSurface = null,
                minRooms = null,
                maxRooms = null,
                minEntryDate = null,
                maxEntryDate = null,
                minSoldDate = null,
                maxSoldDate = null,
                realtyTypes = match { it.isEmpty() },
                realtyTypesSize = 0,
                agentId = null
            )
        } returns listOf(realty)

        val result = repository.getFilteredRealtiesFlow(criteria, isEuro = true).toList().flatten()

        assert(result.size == 1)
        assert(result[0].id == realty.id)
    }

    @Test
    fun `getFilteredRealtiesFlow filters by isAvailable`() = runTest {
        val utils = Utils()
        val realty = utils.createRealty().copy(id = 1, isAvailable = true)
        val criteria = utils.createSearchCriteria().copy(isAvailable = true)

        coEvery {
            mockDao.getFilteredRealties(
                isAvailable = true,
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
                realtyTypes = match { it.isEmpty() },
                realtyTypesSize = 0,
                agentId = null
            )
        } returns listOf(realty)

        val result = repository.getFilteredRealtiesFlow(criteria, isEuro = true).toList().flatten()

        assert(result.size == 1)
        assert(result[0].isAvailable)
    }

    @Test
    fun `getFilteredRealtiesFlow filters by agentId`() = runTest {
        val utils = Utils()
        val realty = utils.createRealty().copy(id = 1, agentId = 42)
        val agent = RealtyAgent(id = 42, name = "Agent 42")
        val criteria = utils.createSearchCriteria().copy(selectedAgent = agent)

        coEvery {
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
                realtyTypes = match { it.isEmpty() },
                realtyTypesSize = 0,
                agentId = 42
            )
        } returns listOf(realty)

        val result = repository.getFilteredRealtiesFlow(criteria, isEuro = true).toList().flatten()

        assert(result.size == 1)
        assert(result[0].agentId == 42)
    }
}