package com.amarchaud.amflowtester.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.amarchaud.amflowtester.R
import com.amarchaud.amflowtester.application.Config
import com.amarchaud.amflowtester.databinding.FragmentDetailsBinding
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.flow.sub.DetailApiFlow
import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import com.amarchaud.amflowtester.model.network.detail.DetailResponse
import com.amarchaud.amflowtester.utils.GlideUtils
import com.amarchaud.amflowtester.viewmodel.DetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // give value to ViewModel
        viewModel.id.value = args.id

        viewModel.loadingLiveData.observe(viewLifecycleOwner, {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.movieLiveData.observe(viewLifecycleOwner, { result: ResultFlow ->

            when(result) {
                is DetailApiFlow ->  {
                    result.details?.let {
                        updateUi(it)
                    }
                }
                is ErrorFlow -> {
                    result.status_message?.let { showError(it) }
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    private fun updateUi(movie: DetailResponse) {
        with(binding) {
            tvTitle.text = movie.title
            tvDescription.text = movie.overview

            GlideUtils.createGlide(requireContext(), Config.IMAGE_URL + movie.poster_path, ivCover)

            val genreNames = mutableListOf<String>()
            movie.genres?.forEach {
                it.name?.let { it1 -> genreNames.add(it1) }
            }
            tvGenre.text = genreNames.joinToString(separator = ", ")
        }
    }
}