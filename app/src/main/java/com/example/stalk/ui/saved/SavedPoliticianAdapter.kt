package com.example.stalk.ui.saved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalk.R

data class Politician(
    val name: String,
    val profilePictureResId: Int,
    val recentTrade: String
)

class SavedPoliticianAdapter(
    private val politicians: List<Politician>,
    private val onProfileButtonClick: (Politician) -> Unit
) : RecyclerView.Adapter<SavedPoliticianAdapter.PoliticianViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliticianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_politician, parent, false)
        return PoliticianViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoliticianViewHolder, position: Int) {
        val politician = politicians[position]
        holder.bind(politician, onProfileButtonClick)
    }

    override fun getItemCount(): Int = politicians.size

    class PoliticianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePicture: ImageView = itemView.findViewById(R.id.profile_picture)
        private val politicianName: TextView = itemView.findViewById(R.id.politician_name)
        private val recentTrade: TextView = itemView.findViewById(R.id.recent_trade)
        private val viewProfileButton: Button = itemView.findViewById(R.id.view_profile_button)

        fun bind(politician: Politician, onProfileButtonClick: (Politician) -> Unit) {
            politicianName.text = politician.name
            profilePicture.setImageResource(politician.profilePictureResId)
            recentTrade.text = politician.recentTrade
            viewProfileButton.setOnClickListener {
                onProfileButtonClick(politician)
            }
        }
    }
}
