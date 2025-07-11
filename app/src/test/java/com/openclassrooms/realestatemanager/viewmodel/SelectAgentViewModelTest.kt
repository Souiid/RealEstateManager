package com.openclassrooms.realestatemanager.viewmodel

import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.features.screens.form.selectagent.SelectAgentViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectAgentViewModelTest {

    private lateinit var viewModel: SelectAgentViewModel
    private val newRealtyRepository = mockk<INewRealtyRepository>(relaxed = true)
    private val agentRepository = mockk<IAgentRepository>(relaxed = true)
    private val realtyRepository = mockk<IRealtyRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val utils = Utils()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SelectAgentViewModel(newRealtyRepository, agentRepository, realtyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agentsFlow returns agents from repository`() = runTest {
        // Arrange
        val agents = listOf(RealtyAgent(1, "John"))
        every { agentRepository.getAllAgents() } returns flowOf(agents)

        // Act
        val result = viewModel.agentsFlow

        // Assert
        result.collect {
            assertEquals(agents, it)
        }
    }

    @Test
    fun `getRealtyPrimaryInfo returns value from newRealtyRepository`() {
        // Arrange
        val primaryInfo = mockk<RealtyPrimaryInfo>()
        every { newRealtyRepository.realtyPrimaryInfo } returns primaryInfo

        // Act
        val result = viewModel.getRealtyPrimaryInfo()

        // Assert
        assertEquals(primaryInfo, result)
    }

    @Test
    fun `getImages returns images from newRealtyRepository`() {
        // Arrange
        val images = listOf(mockk<RealtyPicture>())
        every { newRealtyRepository.images } returns images

        // Act
        val result = viewModel.getImages()

        // Assert
        assertEquals(images, result)
    }

    @Test
    fun `insertRealty calls repository and resets newRealtyRepository`() = runTest {
        // Arrange
        val realty = utils.createRealty()
        coEvery { realtyRepository.insertRealty(realty) } just Runs

        // Act
        viewModel.insertRealty(realty)

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { realtyRepository.insertRealty(realty) }
        verify { newRealtyRepository.images = null }
        verify { newRealtyRepository.realtyPrimaryInfo = null }
    }

    @Test
    fun `insertAgent calls repository`() = runTest {
        // Arrange
        coEvery { agentRepository.insertAgent("John") } just Runs

        // Act
        viewModel.insertAgent("John")

        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { agentRepository.insertAgent("John") }
    }

    @Test
    fun `isAgentNameValid returns true for valid name not in agent list`() {
        // Arrange
        val agents = listOf<RealtyAgent>()
        // Act
        val result = viewModel.isAgentNameValid("John", agents)
        // Assert
        assertTrue(result)
    }

    @Test
    fun `isAgentNameValid returns false when name is shorter than 2 characters`() {
        // Arrange
        val agents = listOf(
            RealtyAgent(1, "John"),
            RealtyAgent(2, "Sarah")
        )
        // Act
        val result = viewModel.isAgentNameValid("A", agents)
        // Assert
        assertFalse(result)
    }

    @Test
    fun `isAgentNameValid returns false when name is already in agent list`() {
        val agents = listOf(
            // Arrange
            RealtyAgent(1, "John")
        )
        // Act
        val result = viewModel.isAgentNameValid("John", agents)
        // Assert
        assertFalse(result)
    }
}