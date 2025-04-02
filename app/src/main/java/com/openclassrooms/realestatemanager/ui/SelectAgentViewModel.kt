package com.openclassrooms.realestatemanager.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.INewRealtyRepository
import com.openclassrooms.realestatemanager.data.RealtyAgent
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.data.RealtyPrimaryInfo
import kotlinx.coroutines.launch

class SelectAgentViewModel(
    val repository: INewRealtyRepository
): ViewModel() {

    val _agents: MutableLiveData<List<RealtyAgent>>? = MutableLiveData(null)
    val agents: LiveData<List<RealtyAgent>>? = _agents

    fun getRealtyPrimaryInfo(): RealtyPrimaryInfo? {
        return repository.realtyPrimaryInfo
    }

    fun getImages(): List<RealtyPicture>? {
        return repository.images
    }

    fun getAllAgents() {
        viewModelScope.launch {
            _agents?.postValue(repository.getAllAgents())
        }
    }

    fun insertAgent(id: Int, name: String) {
        viewModelScope.launch {
            repository.insertAgent(id, name)
        }
    }
}