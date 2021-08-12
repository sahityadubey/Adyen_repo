package com.adyen.android.assignment.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.R
import com.adyen.android.assignment.api.model.RecommendedItem
import com.adyen.android.assignment.api.model.Venue
import kotlinx.android.synthetic.main.location_row_item.view.*

class ListOfVenuesAdapter(context: Context) : RecyclerView.Adapter<ListOfVenuesAdapter.LocationViewHolder>() {
    private var mContext : Context = context
    private var recommendedItems = mutableListOf<RecommendedItem>()

    fun setVenueList(recommendedItems: List<RecommendedItem>) {
        this.recommendedItems = recommendedItems.toMutableList()
        notifyDataSetChanged()
    }

    inner class LocationViewHolder(private var view: View) : RecyclerView.ViewHolder(view){
        fun bind(venue: Venue) {
            view.categoryText.text = mContext.getString(R.string.location_txt, venue.name)
            view.locationText.text = mContext.getString(R.string.category_txt, venue.categories.first().pluralName)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationViewHolder {
        return LocationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.location_row_item, parent,false))
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(recommendedItems[position].venue)
    }

    override fun getItemCount() = recommendedItems.size

    init {
        mContext = context
    }
}