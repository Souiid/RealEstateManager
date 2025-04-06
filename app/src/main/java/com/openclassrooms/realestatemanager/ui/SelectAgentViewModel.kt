package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.IAgentRepository
import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyAgent
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SelectAgentViewModel(
    private val newRealtyRepository: INewRealtyRepository,
    private val agentRepository: IAgentRepository
) : ViewModel() {

    val agentsFlow: Flow<List<RealtyAgent>> = agentRepository.getAllAgents()

    fun getRealtyPrimaryInfo(): RealtyPrimaryInfo? {
        return newRealtyRepository.realtyPrimaryInfo
    }

    fun getImages(): List<RealtyPicture>? {
        return newRealtyRepository.images
    }

    fun insertAgent(name: String) {
        viewModelScope.launch {
            agentRepository.insertAgent(name)
        }
    }
}