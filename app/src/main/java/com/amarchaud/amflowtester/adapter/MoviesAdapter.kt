package com.amarchaud.amflowtester.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.amarchaud.amflowtester.R
import com.amarchaud.amflowtester.application.Config
import com.amarchaud.amflowtester.databinding.ItemListingBinding
import com.amarchaud.amflowtester.interfaces.IMovieClickListener
import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.utils.GenreUtils
import com.amarchaud.amflowtester.utils.GlideUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MoviesAdapter(
    private val onClickListener: IMovieClickListener
) : ListAdapter<MovieEntity, MoviesAdapter.ItemListingViewHolder>(MovieEntity.MovieDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListingViewHolder {
        val binding =
            ItemListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListingViewHolder(binding)
    }


    inner class ItemListingViewHolder(var binding: ItemListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieEntity) = with(binding) {


            itemView.setOnClickListener {
                onClickListener.onMovieClicked(item)
            }


            tvTitle.text = item.title

            GlideUtils.createGlide(itemView.context, Config.IMAGE_URL + item.poster_path, ivPoster)

            tvGenre.text = GenreUtils.getGenre(item.genre_ids)
        }
    }


    override fun onBindViewHolder(holder: ItemListingViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }
}

data class User(val s: String, val i: Int) {

}
