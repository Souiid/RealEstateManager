package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyAgent
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SelectAgentViewModel(
    private val repository: INewRealtyRepository
) : ViewModel() {

    val agentsFlow: Flow<List<RealtyAgent>> = repository.getAllAgents()

    fun getRealtyPrimaryInfo(): RealtyPrimaryInfo? {
        return repository.realtyPrimaryInfo
    }

    fun getImages(): List<RealtyPicture>? {
        return repository.images
    }

    fun insertAgent(name: String) {
        viewModelScope.launch {
            repository.insertAgent(name)
        }
    }
}