package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentPaggingNetworkAndDbDataBinding
import com.vjezba.androidjetpackgithub.ui.adapters.languagerepos.ReposAdapter
import com.vjezba.androidjetpackgithub.ui.adapters.languagerepos.ReposLoadStateAdapter
import com.vjezba.androidjetpackgithub.viewmodels.PaggingWithNetworkAndDbDataViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class PaggingWithNetworkAndDbDataFragment : Fragment() {

    private val viewModel : PaggingWithNetworkAndDbDataViewModel by viewModel()
    private val args: PaggingWithNetworkAndDbDataFragmentArgs by navArgs()

    private val adapter = ReposAdapter()

    private var progressBarRepos: ProgressBar? = null
    private var languageListRepository: RecyclerView? = null

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPaggingNetworkAndDbDataBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.toolbar?.title = getString(R.string.gallery_title) + ": " + args.languageName
        activity?.speedDial?.visibility = View.GONE

        progressBarRepos = binding.progressBarRepositories
        languageListRepository = binding.languageListRepository

        binding.languageListRepository.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.languageListRepository.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBarRepositories.isVisible = loadState.source.refresh is LoadState.Loading

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



        // Scroll to top when the list is refreshed from network.
//        lifecycleScope.launch {
//            adapter.loadStateFlow
//                // Only emit when REFRESH LoadState for RemoteMediator changes.
//                .distinctUntilChangedBy { it.refresh }
//                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
//                .filter { it.refresh is LoadState.NotLoading }
//                .collect { languageListRepository!!.scrollToPosition(0) }
//        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(args.languageName).collectLatest {
                adapter.submitData(it)
            }
        }
    }


}