package com.openclassrooms.realestatemanager.repository

import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.repositories.SearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchRepositoryTest {

    @Test
    fun `criteriaFlow emits null initially`() = runTest {
        // Arrange
        val repository = SearchRepository()

        // Act
        val value = repository.criteriaFlow.first()

        // Assert
        assertEquals(null, value)
    }

    @Test
    fun `saveCriteria updates criteriaFlow`() = runTest {
        // Arrange
        val repository = SearchRepository()
        val criteria = SearchCriteria(
            minPrice = 100000,
            maxPrice = 300000,
            // Arrange
            minSurface = 50,
            maxSurface = 200,
            minRooms = 2,
            maxRooms = 5,
            isAvailable = true,
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

        // Act
        repository.saveCriteria(criteria)

        // Assert
        val value = repository.criteriaFlow.first()

        assertEquals(criteria, value)
    }
}
