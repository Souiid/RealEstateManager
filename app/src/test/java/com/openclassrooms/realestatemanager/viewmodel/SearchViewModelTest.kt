package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.ISearchRepository
import com.openclassrooms.realestatemanager.features.screens.search.SearchViewModel
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class SearchViewModelTest {

    private val agentRepository = mockk<IAgentRepository>()
    private val searchRepository = mockk<ISearchRepository>(relaxed = true)
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        every { agentRepository.getAllAgents() } returns flowOf(emptyList())
        viewModel = SearchViewModel(agentRepository, searchRepository)
    }

    @Test
    fun `setCriteria updates criteriaFlow and saves criteria`() {
        // Arrange
        val criteria = SearchCriteria()

        // Act
        viewModel.setCriteria(criteria)

        // Assert
        assertEquals(criteria, viewModel.criteriaFlow.value)
        verify { searchRepository.saveCriteria(criteria) }
    }


    @Test
    fun `validateCriteria returns error if minPrice greater than maxPrice`() {
        //Arrange
        val context = mockk<Context> {
            every { getString(R.string.error_price_range) } returns "Min price > max price"
        }

        // Act
        val error = viewModel.validateCriteria(
            context,
            minPrice = 1000,
            maxPrice = 500,
            minSurface = null, maxSurface = null,
            minRooms = null, maxRooms = null,
            minEntryDate = null, maxEntryDate = null,
            minSoldDate = null, maxSoldDate = null,
        )

        // Assert
        assertEquals("Min price > max price", error)
    }

    @Test
    fun `validateCriteria returns error if minSurface greater than maxSurface`() {
        //Arrange
        val context = mockk<Context> {
            every { getString(R.string.error_surface_range) } returns "Min surface > max surface"
        }

        // Act
        val error = viewModel.validateCriteria(
            context,
            minPrice = null, maxPrice = null,
            minSurface = 100, maxSurface = 50,
            minRooms = null, maxRooms = null,
            minEntryDate = null, maxEntryDate = null,
            minSoldDate = null, maxSoldDate = null,
        )

        // Assert
        assertEquals("Min surface > max surface", error)
    }

    @Test
    fun `validateCriteria returns error if minRooms greater than maxRooms`() {
        //Arrange
        val context = mockk<Context> {
            every { getString(R.string.error_room_range) } returns "Min rooms > max rooms"
        }

        // Act
        val error = viewModel.validateCriteria(
            context,
            null, null,
            null, null,
            minRooms = 4, maxRooms = 2,
            minEntryDate = null, maxEntryDate = null,
            minSoldDate = null, maxSoldDate = null,
        )

        // Assert
        assertEquals("Min rooms > max rooms", error)
    }

    @Test
    fun `validateCriteria returns null when all ranges are valid`() {
        // Arrange
        val context = mockk<Context>()

        // Act
        val error = viewModel.validateCriteria(
            context,
            minPrice = 500, maxPrice = 1000,
            minSurface = 30, maxSurface = 100,
            minRooms = 1, maxRooms = 3,
            minEntryDate = Date(1000), maxEntryDate = Date(2000),
            minSoldDate = Date(3000), maxSoldDate = Date(4000),
        )

        // Assert
        assertNull(error)
    }
}