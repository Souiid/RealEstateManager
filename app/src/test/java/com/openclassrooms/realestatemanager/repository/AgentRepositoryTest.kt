package com.openclassrooms.realestatemanager.repository

import android.content.Context
import com.openclassrooms.realestatemanager.data.repositories.AgentRepository
import com.openclassrooms.realestatemanager.data.room.AppDatabase
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.dao.RealtyAgentDao
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import io.mockk.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AgentRepositoryTest {

    private lateinit var repository: AgentRepository
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockDb = mockk<AppDatabase>(relaxed = true)
    private val mockDao = mockk<RealtyAgentDao>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(DatabaseProvider)
        every { DatabaseProvider.getDatabase(mockContext) } returns mockDb
        every { mockDb.realtyAgentDao() } returns mockDao

        repository = AgentRepository(mockContext)
    }

    @Test
    fun `insertAgent calls dao insertAgent`() = runTest {

        // Act
        repository.insertAgent("John Doe")

        // Assert
        coVerify {
            mockDao.insertAgent(match { it.name == "John Doe" })
        }
    }

    @Test
    fun `getAllAgents returns dao flow`() = runTest {
        // Arrange
        val expectedFlow = mockk<Flow<List<RealtyAgent>>>()
        every { mockDao.getAllAgents() } returns expectedFlow

        // Act
        val result = repository.getAllAgents()

        // Assert
        assert(result === expectedFlow)
        verify { mockDao.getAllAgents() }
    }

    @Test
    fun `getAgentByID calls dao getAgentById`() = runTest {
        // Arrange
        val agent = RealtyAgent(id = 1, name = "John")
        coEvery { mockDao.getAgentById(1) } returns agent

        // Act
        val result = repository.getAgentByID(1)

        // Assert
        assert(result == agent)
        coVerify { mockDao.getAgentById(1) }
    }
}




