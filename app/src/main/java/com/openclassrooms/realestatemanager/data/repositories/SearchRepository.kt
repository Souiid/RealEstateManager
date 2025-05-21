package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.SearchCriteria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchRepository : ISearchRepository {

    private val _criteriaFlow = MutableStateFlow<SearchCriteria?>(null)
    override val criteriaFlow: StateFlow<SearchCriteria?> = _criteriaFlow

    override fun saveCriteria(criteria: SearchCriteria) {
        _criteriaFlow.value = criteria
    }
}