package com.amarchaud.amflowtester.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amarchaud.amflowtester.R
import com.amarchaud.amflowtester.application.Config
import com.amarchaud.amflowtester.databinding.ItemListingBinding
import com.amarchaud.amflowtester.interfaces.IMovieClickListener
import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.utils.GenreUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MoviesAdapter(
    private val movieList: ArrayList<MovieEntity>,
    private val onClickListener: IMovieClickListener
) : RecyclerView.Adapter<MoviesAdapter.ItemListingViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListingViewHolder {
        val binding =
            ItemListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListingViewHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size

    inner class ItemListingViewHolder(var binding: ItemListingBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: ItemListingViewHolder, position: Int) {
        val context = holder.itemView.context
        val movie = movieList[position]

        holder.itemView.setOnClickListener {
            onClickListener.onMovieClicked(movie)
        }

        with(holder.binding) {
            tvTitle.text = movie.title
            Glide.with(context).load(Config.IMAGE_URL + movie.poster_path)
                .apply(
                    RequestOptions().override(400, 400).centerInside()
                        .placeholder(R.drawable.placehoder)
                ).into(ivPoster)
            tvGenre.text = GenreUtils.getGenre(movie.genre_ids)
        }
    }

    fun updateData(newList: List<MovieEntity>) {
        movieList.clear()
        val sortedList = newList.sortedBy { movie -> movie.popularity }
        movieList.addAll(sortedList)
        notifyDataSetChanged()
    }
}