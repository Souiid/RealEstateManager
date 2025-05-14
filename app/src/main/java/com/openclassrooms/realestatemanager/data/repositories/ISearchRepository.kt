package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.SearchCriteria

interface ISearchRepository {
    var currentCriteria: SearchCriteria?

    fun saveCriteria(criteria: SearchCriteria)
}