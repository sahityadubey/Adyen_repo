package com.adyen.android.assignment.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adyen.android.assignment.api.model.RecommendedItem
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.api.model.VenueRecommendationsResponse
import com.adyen.android.assignment.api.repository.LocationRepository
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class LocationViewModel : ViewModel() {
    val venueList = MutableLiveData<List<RecommendedItem>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllVenueNearBy(currentLocation: Location?) {
        val response = LocationRepository.getInstance().getVenueByLocation(currentLocation)
        response.enqueue(object: Callback<ResponseWrapper<VenueRecommendationsResponse>> {
            override fun onResponse(call: Call<ResponseWrapper<VenueRecommendationsResponse>>, response: Response<ResponseWrapper<VenueRecommendationsResponse>>) {
                venueList.postValue(response.body()?.response?.groups?.first()?.items)
            }

            override fun onFailure(call: Call<ResponseWrapper<VenueRecommendationsResponse>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
