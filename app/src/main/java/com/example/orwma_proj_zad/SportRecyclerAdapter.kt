package com.example.orwma_proj_zad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle

enum class ItemClickType {
    SPORT
}

class SportRecyclerAdapter(private val items: ArrayList<Sport>, private val listener: ContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SportViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.sports_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SportViewHolder -> {
                holder.bind(items[position], listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    class SportViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val sportGroup = itemView.findViewById<TextView>(R.id.sportGroupText)
        private val sportDescription = itemView.findViewById<TextView>(R.id.sportDescriptionText)
        private val sportTitle = itemView.findViewById<TextView>(R.id.sportTitleText)
        private val forwardButton = itemView.findViewById<ImageButton>(R.id.forwardButton)


        fun bind(sport: Sport, listener: ContentListener) {
            sportGroup.text = sport.group
            sportDescription.text = sport.description
            sportTitle.text = sport.title

            forwardButton.setOnClickListener{
                listener.onItemButtonClick(sport, ItemClickType.SPORT)
            }
        }
    }

    interface ContentListener {
        fun onItemButtonClick(sport: Sport, clickType: ItemClickType)
    }
}