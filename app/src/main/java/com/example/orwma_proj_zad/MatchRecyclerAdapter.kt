package com.example.orwma_proj_zad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import kotlin.time.Duration

enum class MatchClick{
    FAVORITE
}

class MatchRecyclerAdapter(private val items: ArrayList<Match>, private val listener: ContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MatchViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.matches_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MatchViewHolder -> {
                holder.bind(position, items[position], listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    class MatchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val homeTeamName = itemView.findViewById<TextView>(R.id.homeTeamNameText)
        private val awayTeamName = itemView.findViewById<TextView>(R.id.awayTeamNameText)
        private val homeTeamScore = itemView.findViewById<TextView>(R.id.homeTeamScore)
        private val awayTeamScore = itemView.findViewById<TextView>(R.id.awayTeamScore)
        private val matchStartTime = itemView.findViewById<TextView>(R.id.matchStartTime)
        private val favoriteButton = itemView.findViewById<ImageButton>(R.id.favoriteButton)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")


        fun bind(index: Int, match: Match, listener: ContentListener){
            homeTeamName.text = match.home_team
            awayTeamName.text = match.away_team
            matchStartTime.text = format.parse(match.commence_time).toString()
            if(match.scores==null){
                homeTeamScore.text = ""
                awayTeamScore.text = ""
            } else{
                homeTeamScore.text = match.scores[0].score.toString()
                awayTeamScore.text = match.scores[1].score.toString()
            }

            favoriteButton.setOnClickListener{
                listener.onItemButtonClick(index, match, MatchClick.FAVORITE)
            }
        }
    }

    interface ContentListener {
        fun onItemButtonClick(index: Int, match: Match, clickType: MatchClick)
    }

}