package com.example.moviestudyapp.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.moviestudyapp.Constants
import com.example.moviestudyapp.databinding.FragmentSearchBinding
import com.example.moviestudyapp.presentation.BaseFragment
import com.example.moviestudyapp.presentation.adapter.SearchMovieAdapter
import com.example.moviestudyapp.presentation.movie_detail.MovieDetailActivity
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class SearchFragment : BaseFragment<SearchMovieViewModel>(), CoroutineScope {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var adapter : SearchMovieAdapter

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val viewModel: SearchMovieViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentSearchBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun observeData() {
        viewModel.searchMovieLiveData.observe(this){
            when(it){
                is SearchMovieState.UnInitialized -> {
                    initViews()
                }
                is SearchMovieState.SelectKeywordSuccess -> {
                    handleSelectKeywordSuccessState(it)
                }
                is SearchMovieState.Loading -> {
                    handleLoadingState()
                }
                is SearchMovieState.Success -> {
                    handleSuccessState(it)
                }
                is SearchMovieState.DeleteSuccess -> {
                    handleDeleteSuccessState(it)
                }
                is SearchMovieState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun initViews() = with(binding){
        settingInitVisibleViews()

        btnSearch.setOnClickListener {
            val query = editSearch.text.toString().trim()

            if (query.isEmpty()){
                return@setOnClickListener
            }

            viewModel.searchMove(query)
        }

        adapter = SearchMovieAdapter(requireContext()){ movieId ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java)
            intent.putExtra(Constants.INTENT_MOVIE_ID, movieId)
            startActivity(intent)
        }

        rvSearchMovie.adapter = adapter
    }

    private fun handleSelectKeywordSuccessState(searchMovieState: SearchMovieState.SelectKeywordSuccess) = with(binding){
        progressBar.isVisible = false
        chipGroup.removeAllViews()

        searchMovieState.keywordList.forEach { keyword ->
            chipGroup.addView(createChipView(keyword))
        }

        settingKeywordViewsVisible()
    }

    private fun handleLoadingState() = with(binding){
        progressBar.isVisible = true
    }

    private fun handleSuccessState(searchMovieState: SearchMovieState.Success) = with(binding) {
        if(this@SearchFragment::adapter.isInitialized.not()){
            initViews()
        }
        settingSuccessVisibleViews()
        adapter.submitList(searchMovieState.movieSearchResult.results)
        if(searchMovieState.movieSearchResult.results.isEmpty()){
            txtError.isVisible = true
        }

        viewModel.fetchData()
    }

    private fun handleDeleteSuccessState(searchMovieState: SearchMovieState.DeleteSuccess) = with(binding){
        if (searchMovieState.count > 0){
            chipGroup.removeView(searchMovieState.cipView)
            settingKeywordViewsVisible()
        }
    }

    private fun handleErrorState() = with(binding){
        settingErrorVisibleViews()
    }

    private fun settingInitVisibleViews() = with(binding){
        progressBar.isVisible = false
        editSearch.isVisible = true
        btnSearch.isVisible = true
        txtGuide.isVisible= true

        txtError.isVisible = false
        btnError.isVisible = false
    }

    private fun settingSuccessVisibleViews() = with(binding){
        progressBar.isVisible = false
        editSearch.isVisible = true
        btnSearch.isVisible = true
        txtGuide.isVisible= false

        txtError.isVisible = false
        btnError.isVisible = false
    }

    private fun settingErrorVisibleViews() = with(binding){
        progressBar.isVisible = false
        editSearch.isVisible = false
        btnSearch.isVisible = false
        txtGuide.isVisible= false

        txtError.isVisible = true
        btnError.isVisible = true
    }

    private fun settingKeywordViewsVisible() = with(binding){
        chipGroup.isGone = chipGroup.childCount == 0
        txtRecentSearches.isGone = chipGroup.isEmpty()
    }

    private fun createChipView(keyword : String) =
        Chip(requireContext()).apply {
            text = keyword
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                viewModel.deleteKeyword("$text", this)
            }
            setOnClickListener {
                binding.editSearch.setText("$text")
                binding.btnSearch.performClick()
            }
        }

}