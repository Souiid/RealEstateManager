package com.openclassrooms.realestatemanager.ui.screens

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent

class RealtyDescriptionViewModel(
    private val realtyRepository: IRealtyRepository,
    private val agentRepository: IAgentRepository
) : ViewModel() {

    fun getSelectedRealty(): Realty? {
        return realtyRepository.selectedRealty
    }

    suspend fun getAgentRepository(agentId: Int): RealtyAgent? {
        return agentRepository.getAgentByID(agentId)
    }

}