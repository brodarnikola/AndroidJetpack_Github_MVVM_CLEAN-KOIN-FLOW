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
import com.vjezba.androidjetpackgithub.ui.adapters.GalleryAdapter
import com.vjezba.androidjetpackgithub.viewmodels.LanguagesListViewModel
import com.vjezba.androidjetpackgithub.viewmodels.PaggingWithNetworkAndDbDataViewModel
import com.vjezba.androidjetpackgithub.viewmodels.PaggingWithNetworkAndDbViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.getStateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PaggingWithNetworkAndDbDataFragment : Fragment() {

    private val viewModel by lazy {
        getStateViewModel<PaggingWithNetworkAndDbDataViewModel>()
    }
    private val args: PaggingWithNetworkAndDbDataFragmentArgs by navArgs()

    private val adapter =
        GalleryAdapter()

    private var progressBarRepos: ProgressBar? = null
    private var languageListRepository: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPaggingNetworkAndDbDataBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.toolbar?.title = getString(R.string.gallery_title) + ": " + args.languageName

        viewModel.showSubreddit(args.languageName)


        progressBarRepos = binding.progressBarRepositories
        languageListRepository = binding.languageListRepository

        binding.languageListRepository.adapter = adapter

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
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            viewModel.posts.collectLatest {
                adapter.submitData(it)
            }
        }
    }


}