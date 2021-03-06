package com.amarchaud.amflowtester.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amarchaud.amflowtester.adapter.MoviesAdapter
import com.amarchaud.amflowtester.databinding.FragmentListingBinding
import com.amarchaud.amflowtester.interfaces.IMovieClickListener
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import com.amarchaud.amflowtester.model.flow.sub.InitFlow
import com.amarchaud.amflowtester.model.flow.sub.MovieEntityFlow
import com.amarchaud.amflowtester.viewmodel.ListingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListingFragment : Fragment(), IMovieClickListener {

    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListingViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerViewMovies.layoutManager = layoutManager

            val dividerItemDecoration = DividerItemDecoration(
                recyclerViewMovies.context,
                layoutManager.orientation
            )

            recyclerViewMovies.addItemDecoration(dividerItemDecoration)
            moviesAdapter = MoviesAdapter(this@ListingFragment)
            recyclerViewMovies.adapter = moviesAdapter
        }

        binding.mainSwipeRefresh.setOnRefreshListener {
            viewModel.fetchMovies()
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner, {
            binding.run {
                if (it) {
                    loading.visibility = View.VISIBLE
                    recyclerViewMovies.visibility = View.GONE
                } else {
                    loading.visibility = View.GONE
                    recyclerViewMovies.visibility = View.VISIBLE
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.movieListStateFlow.collect { result: ResultFlow ->

                when (result) {

                    is InitFlow -> {
                        binding.mainSwipeRefresh.isRefreshing = false
                    }

                    is MovieEntityFlow -> {
                        result.movies?.let {
                            moviesAdapter.submitList(it)
                        }
                    }

                    is ErrorFlow -> {
                        result.status_message?.let { showError(it) }
                    }
                }
            }
        }

        viewModel.movieListLiveData.observe(viewLifecycleOwner, { result: ResultFlow ->

            binding.mainSwipeRefresh.isRefreshing = false

            when (result) {

                is MovieEntityFlow -> {
                    result.movies?.let {
                        moviesAdapter.submitList(it)
                    }
                }

                is ErrorFlow -> {
                    result.status_message?.let { showError(it) }
                }
            }
        })

        if (viewModel.movieListLiveData.value == null) {
            viewModel.fetchMovies()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    override fun onMovieClicked(movie: MovieEntity) {
        findNavController().navigate(
            ListingFragmentDirections.actionListingFragmentToDetailsFragment(
                movie.id
            )
        )
    }

}