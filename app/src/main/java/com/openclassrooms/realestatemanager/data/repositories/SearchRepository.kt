package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.SearchCriteria

class SearchRepository : ISearchRepository {

    override var currentCriteria: SearchCriteria? = null

    override fun saveCriteria(criteria: SearchCriteria) {
        currentCriteria = criteria
    }
}