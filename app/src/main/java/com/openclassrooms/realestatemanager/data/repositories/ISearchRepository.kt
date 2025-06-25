package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.SearchCriteria
import kotlinx.coroutines.flow.StateFlow

interface ISearchRepository {
    val criteriaFlow: StateFlow<SearchCriteria?>
    fun saveCriteria(criteria: SearchCriteria)
}