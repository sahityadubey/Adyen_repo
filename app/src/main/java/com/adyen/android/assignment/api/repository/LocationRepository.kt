package com.adyen.android.assignment.api.repository

import android.location.Location
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.api.model.VenueRecommendationsResponse
import retrofit2.Call

class LocationRepository {

    companion object {
        fun getInstance() = LocationRepository()
    }

    fun getVenueByLocation(coordinates: Location?): Call<ResponseWrapper<VenueRecommendationsResponse>> {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(coordinates?.latitude!!, coordinates.longitude).build()
        return PlacesService.instance.getVenueRecommendations(query)
    }
}