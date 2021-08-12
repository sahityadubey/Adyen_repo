package com.adyen.android.assignment.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.model.RecommendedItem
import com.adyen.android.assignment.factory.ViewModelFactory
import com.adyen.android.assignment.ui.adapter.ListOfVenuesAdapter
import com.adyen.android.assignment.utilities.showToast
import com.adyen.android.assignment.viewmodel.LocationViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var locationViewModel: LocationViewModel
    private var locationAdapter = ListOfVenuesAdapter(this)
    private var mFusedLocationClient: FusedLocationProviderClient? =  null
    private var matchedData = arrayListOf<RecommendedItem>()
    private var actualData = listOf<RecommendedItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeView()
    }

    override fun onResume() {
        super.onResume()

        captureLocation()
    }

    private fun initializeView() {
        initLocationProvider()
        initViewModel()
        initAdapter()
        observeData()
        performSearch()
    }

    private fun initLocationProvider() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun observeData() {
        locationViewModel.venueList.observe(this, Observer {
            Log.d(this::getLocalClassName.toString(), "onCreate: $it")
            actualData = it
            locationAdapter.setVenueList(it)
        })

        locationViewModel.errorMessage.observe(this, Observer {
           it.showToast(this)
        })
    }

    private fun initAdapter() {
        venueRecyclerView.adapter = locationAdapter
        searchView.isSubmitButtonEnabled = true
    }

    private fun initViewModel() {
        locationViewModel = ViewModelProvider(this, ViewModelFactory()).get(LocationViewModel::class.java)
    }

    private fun performSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }

    private fun search(text: String?) {
        matchedData = arrayListOf()
        text?.let {
            actualData.forEach { data ->
                if (data.venue.name.contains(text, true) ||
                    data.venue.categories.first().shortName.contains(text, true)
                ) {
                    matchedData.add(data)
                    updateRecyclerView()
                }
            }
            if (matchedData.isEmpty()) {
                "No match found!".showToast(this)
            }
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        venueRecyclerView.apply {
            locationAdapter.setVenueList(matchedData)
        }
    }

    @SuppressLint("MissingPermission")
    fun captureLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful && task.result != null) {
                            locationViewModel.getAllVenueNearBy(task.result)
                        } else {
                            Log.w("TAG", "getLastLocation:exception", task.exception)
                        }
                    }

            } else {
                "Turn on location".showToast(this)
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                this.startActivity(intent)
            }
        } else{
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                captureLocation()
            }
        }
    }
}
