package com.openclassrooms.realestatemanager.viewmodel

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.features.screens.main.descrpition.RealtyDescriptionViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
class RealtyDescriptionViewModelTest {

    private lateinit var viewModel: RealtyDescriptionViewModel
    private val realtyRepository: IRealtyRepository = mockk(relaxed = true)
    private val agentRepository: IAgentRepository = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val utils = Utils()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Places::class)
        every { Places.initialize(any(), any()) } just Runs
        every { Places.createClient(any()) } returns mockk()

        every { realtyRepository.selectedRealtyFlow } returns MutableStateFlow(null)

        viewModel = RealtyDescriptionViewModel(context, realtyRepository, agentRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getRealtyFromID fetches and sets selected realty`() = runTest {
        val realty = utils.createRealty()
        coEvery { realtyRepository.getRealtyFromID(1) } returns realty
        coEvery { realtyRepository.setSelectedRealty(realty) } just Runs

        viewModel.getRealtyFromID(1)

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { realtyRepository.getRealtyFromID(1) }
        coVerify { realtyRepository.setSelectedRealty(realty) }
    }

    @Test
    fun `updateRealtyStatus updates and sets selected realty`() = runTest {
        val realty = utils.createRealty()
        coEvery { realtyRepository.updateRealty(realty) } just Runs
        coEvery { realtyRepository.setSelectedRealty(realty) } just Runs

        viewModel.updateRealtyStatus(realty)

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { realtyRepository.updateRealty(realty) }
        coVerify { realtyRepository.setSelectedRealty(realty) }
    }

    @Test
    fun `getAgentRepository returns correct agent`() = runTest {
        val agent = RealtyAgent(1, "Alice")
        coEvery { agentRepository.getAgentByID(1) } returns agent

        val result = viewModel.getAgentRepository(1)

        assertEquals(agent, result)
    }

    @Test
    fun `setSelectedRealty sets realty in repository`() {
        val realty = utils.createRealty()
        every { realtyRepository.setSelectedRealty(realty) } just Runs

        viewModel.setSelectedRealty(realty)

        verify { realtyRepository.setSelectedRealty(realty) }
    }
}