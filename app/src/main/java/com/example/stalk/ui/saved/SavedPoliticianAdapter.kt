package com.example.stalk.ui.saved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stalk.R
import com.example.stalk.model.Name

class SavedPoliticianAdapter(
    private var politicians: List<Name>,
    private val onProfileButtonClick: (Name) -> Unit
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

    fun updatePoliticians(newPoliticians: List<Name>) {
        politicians = newPoliticians
        notifyDataSetChanged()
    }

    class PoliticianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profilePicture: ImageView = itemView.findViewById(R.id.profile_picture)
        private val politicianName: TextView = itemView.findViewById(R.id.politician_name)
        private val viewProfileButton: Button = itemView.findViewById(R.id.view_profile_button)

        fun bind(politician: Name, onProfileButtonClick: (Name) -> Unit) {
            politicianName.text = "${politician.firstName} ${politician.lastName}"
            Glide.with(itemView.context)
                .load(politician.img)
                .error(R.drawable.ic_profile_placeholder)
                .into(profilePicture)
            viewProfileButton.setOnClickListener {
                onProfileButtonClick(politician)
            }
        }
    }
}
