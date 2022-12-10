package com.algorithm_termproject.travelmate.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.algorithm_termproject.travelmate.data.Place
import com.algorithm_termproject.travelmate.databinding.ItemPlaceBinding

class PlaceRVAdapter : RecyclerView.Adapter<PlaceRVAdapter.ViewHolder>() {
    private val placeList = arrayListOf<Place>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position+1, placeList[position])
    }

    override fun getItemCount(): Int = placeList.size


    inner class ViewHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(idx: Int, place: Place){
            binding.itemPlaceNumberTv.text = idx.toString()
            binding.itemPlaceTitleTv.text = place.name
            binding.itemPlaceAddressTv.text = place.address

            // Click listener
            binding.itemPlaceDeleteIv.setOnClickListener {
                deletePlace(idx, place)
            }
        }
    }

    /* 데이터 */
    fun addPlace(place: Place){
        placeList.add(place)
        notifyDataSetChanged()

        myItemChangedListener.onItemAdded(place, placeList.size)
    }

    private fun deletePlace(index: Int, place: Place){
        placeList.remove(place)
        notifyDataSetChanged()

        myItemChangedListener.onItemDeleted(index, place, placeList.size)
    }

    fun getPlaceList(): ArrayList<Place>{
        return placeList
    }

    /* Change Listener */
    private lateinit var myItemChangedListener: MyItemChangedListener

    interface MyItemChangedListener {
        fun onItemAdded(place: Place, size: Int)
        fun onItemDeleted(index: Int, place: Place, size: Int)
    }

    fun setMyItemClickListener(myItemChangedListener: MyItemChangedListener) {
        this.myItemChangedListener = myItemChangedListener
    }
}