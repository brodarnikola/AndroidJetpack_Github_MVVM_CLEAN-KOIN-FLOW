/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.vjezba.androidjetpackgithub.databinding.FragmentFlowMultipleExamplesBinding
import com.vjezba.androidjetpackgithub.ui.adapters.FlowMultipleExamplesAdapter
import com.vjezba.androidjetpackgithub.viewmodels.FlowMultipleExamplesViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_flow_multiple_examples.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class FlowMultipleExampleFragment : Fragment() {

    private val homeViewModel: FlowMultipleExamplesViewModel by viewModel()

    private val flowMultipleExamplesAdapter by lazy { FlowMultipleExamplesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFlowMultipleExamplesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initUi()
    }

    private fun initUi() {
        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {

        homeViewModel.postData.observe(viewLifecycleOwner, Observer {
            flowMultipleExamplesAdapter.setPosts(it.toMutableList())
            progressBarFlow.visibility = View.GONE
        })

        homeViewModel.commentData.observe(viewLifecycleOwner, Observer {
            flowMultipleExamplesAdapter.updatePost(it)
        })

        lifecycleScope.launch {
            flowExamples(this)
        }
    }

    private fun initRecyclerView() {

        list_repos_flat_map.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        list_repos_flat_map.adapter = flowMultipleExamplesAdapter
    }


    private suspend fun flowExamples(coroutineScope: CoroutineScope) {

        combine(f1, f2, f3) { list, list2, list3 ->
            list + list2 + list3
        }
            // print -> [1, 2, 3, 4, 5, 6]
            .onEach {
                println(it)
            }
            .launchIn(coroutineScope)

        (1..3).asFlow().map { requestFlow(it) }
            .onEach { stringData -> println("Data of flow is: " + stringData) }
            .collect()

        // launchIn means, we are collecting data asinkron
        // collect, collectLatest and so on.. means, we are collecting data sinkrono

        exampleOfAsynchronFunction(coroutineScope)// this is asincron function because of operator launchIn
        exampleOfSyncronFunction() // this is sincrono function because of operator collect

        println(
            "Only when this this two above function 'exampleOfAsynchronFunction' and 'exampleOfSyncronFunction'" +
                    "  are done, then only it will be executed this line" +
                    "\n Because this function 'exampleOfSyncronFunction' is suspending function of, because of operator 'collect'  "
        )
    }

    val f1 = flow {
        emit(listOf(1, 2))
    }

    val f2 = flow {
        emit(listOf(3, 4))
    }

    val f3 = flow {
        emit(listOf(5, 6))
    }

    private suspend fun exampleOfSyncronFunction() {
        val nums1 = (1..3).asFlow().onEach { delay(300) }  // numbers 1..4
        val strs1 = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums1.combine(strs1) { a, b -> "$a -> $b" } // compose a single string
            .collect { println("COMBINE operator: Data of two combines flows ( nums and strs ) is: " + it) }
    }

    private fun exampleOfAsynchronFunction(coroutineScope: CoroutineScope) {
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..4
        val strs = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string
            .onEach { println("ZIP operator: Data of two combines flows ( nums and strs ) is: " + it) }
            .launchIn(coroutineScope)
    }

    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

}
