package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import com.openclassrooms.realestatemanager.data.SearchCriteria
import com.openclassrooms.realestatemanager.data.Utils
import com.openclassrooms.realestatemanager.data.room.DatabaseProvider
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RealtyRepository(context: Context): IRealtyRepository {

    private val db = DatabaseProvider.getDatabase(context)
    private val dao = db.realtyDao()
    override var filteredRealties = emptyList<Realty>()
    private val _selectedRealty = MutableStateFlow<Realty?>(null)
    override val selectedRealtyFlow: StateFlow<Realty?> = _selectedRealty
    override var updatedRealty: Realty? = null

    override fun setSelectedRealty(realty: Realty?) {
        _selectedRealty.value = realty
    }

    override suspend fun insertRealty(realty: Realty) {
        dao.insertRealty(realty)
    }

    override fun getAllRealties(): Flow<List<Realty>> {
        return dao.getAllRealties()
    }

    override suspend fun getRealtyFromID(realtyID: Int): Realty? {
        return db.realtyDao().getRealtyById(realtyID.toString())
    }

    override suspend fun updateRealty(realty: Realty) {
        dao.updateRealty(realty)
    }

    override fun getFilteredRealtiesFlow(criteria: SearchCriteria?, isEuro: Boolean): Flow<List<Realty>> = flow {
        val results = dao.getFilteredRealties(
            isAvailable = criteria?.isAvailable,
            minPrice = null,
            maxPrice = null,
            minSurface = criteria?.minSurface?.toDouble(),
            maxSurface = criteria?.maxSurface?.toDouble(),
            minRooms = criteria?.minRooms,
            maxRooms = criteria?.maxRooms,
            minEntryDate = criteria?.minEntryDate,
            maxEntryDate = criteria?.maxEntryDate,
            minSoldDate = criteria?.minSoldDate,
            maxSoldDate = criteria?.maxSoldDate,
            realtyTypes = criteria?.realtyTypes?.map { it.name },
            realtyTypesSize = criteria?.realtyTypes?.size ?: 0,
            agentId = criteria?.selectedAgent?.id
        )

        val filtered = results.filter { realty ->
            val realtyPriceInUserCurrency = if (realty.primaryInfo.isEuro == (isEuro)) {
                realty.primaryInfo.price
            } else {
                if (isEuro) {
                    Utils().convertDollarToEuro(realty.primaryInfo.price)
                } else {
                    Utils() convertEuroToDollar(realty.primaryInfo.price)
                }
            }

            val priceOk =
                (criteria?.minPrice == null || realtyPriceInUserCurrency >= criteria.minPrice) &&
                        (criteria?.maxPrice == null || realtyPriceInUserCurrency <= criteria.maxPrice)

            val amenitiesOk = criteria?.amenities.isNullOrEmpty() ||
                    criteria!!.amenities.all { it in realty.primaryInfo.amenities }

            priceOk && amenitiesOk
        }

        emit(filtered)
    }.flowOn(Dispatchers.IO)


}