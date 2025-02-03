package com.myappseries

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.myappseries.databinding.ItemserieBinding
import com.myappseries.model.Serie
import com.squareup.picasso.Picasso

class SerieViewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val binding = ItemserieBinding.bind(view)

    fun bind(serie: Serie){
        Picasso.get().load(serie.image).into(binding.ivSerie)
        binding.tvTitle.setText("Titulo: "+serie.title)
        binding.tvCreator.setText("Creador: " + serie.creator)
        binding.tvRating.setText("Puntuación: "+ serie.rating.toString())
        binding.tvChannel.setText("Donde ver: " + serie.channel)
        binding.tvDates.setText("Fechas de emisión: " + serie.dates)
    }

}