package com.openclassrooms.realestatemanager.ui.screens.form.selectagent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.repositories.IAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.RealtyAgent
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import com.openclassrooms.realestatemanager.data.repositories.IRealtyRepository
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SelectAgentViewModel(
    private val newRealtyRepository: INewRealtyRepository,
    private val agentRepository: IAgentRepository,
    private val realtyRepository: IRealtyRepository
) : ViewModel() {

    val agentsFlow: Flow<List<RealtyAgent>> = agentRepository.getAllAgents()

    fun getRealtyPrimaryInfo(): RealtyPrimaryInfo? {
        return newRealtyRepository.realtyPrimaryInfo
    }

    fun getImages(): List<RealtyPicture>? {
        return newRealtyRepository.images
    }

    fun insertRealty(realty: Realty) {
        viewModelScope.launch {
            realtyRepository.insertRealty(realty)
            newRealtyRepository.images = null
            newRealtyRepository.realtyPrimaryInfo = null
        }
    }

    fun insertAgent(name: String) {
        viewModelScope.launch {
            agentRepository.insertAgent(name)
        }
    }

    fun isAgentNameValid(name: String): Boolean {
        return name.trim().length >= 2
    }
}