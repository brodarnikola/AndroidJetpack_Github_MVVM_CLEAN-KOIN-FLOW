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
import java.util.*

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

        var counter0 = 1
        homeViewModel.postData.observe(viewLifecycleOwner, Observer {
            flowMultipleExamplesAdapter.setPosts(it.toMutableList())
            progressBarFlow.visibility = View.GONE
            Log.d("HowManyTimes", "1111 How many times it will enter here: ${counter0}")
            counter0++
        })

        var counter1 = 1
        homeViewModel.commentData.observe(viewLifecycleOwner, Observer {
            flowMultipleExamplesAdapter.updatePost(it)
            Log.d("HowManyTimes", "2222 How many times it will enter here: ${counter1}")
            counter1++
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

        // combine is a asynchron way of combining this 3 flows, coroutines
        combine(f1, f2, f3) { list1, list2, list3 ->
            list1 + list2 + list3
        }
            // print -> [1, 2, 3, 4, 5, 6], because of this line of code list1 + list2 + list3
                // because first we are printing data from list1,
                // after that we are printing data from list2 and in the end from list3
            .onEach {
                println( "Using combine operator of flow: " + it)
            }
            .launchIn(coroutineScope)

        (1..3).asFlow().map { requestFlow(it) }
            .onEach { stringData -> println("Data of flow is: " + stringData) }
            .collect()

        exampleOfFlatMapConcatFlow(coroutineScope)
        exampleOfFlatMapMergeFlow(coroutineScope)
        exampleOfFlatMapLatestFlow(coroutineScope)

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

    private suspend fun exampleOfFlatMapConcatFlow(coroutineScope: CoroutineScope) {
        val startTime = System.currentTimeMillis()
        (1..3).asFlow().onEach { delay(100) }
            .flatMapConcat {
                requestFlow(it)
            }
            .map {
                println("Example of flatMapConcat: $it at ${System.currentTimeMillis() - startTime} ms from start")
            }
            .launchIn(coroutineScope)
    }

    private fun exampleOfFlatMapMergeFlow(coroutineScope: CoroutineScope) {
        val startTime = System.currentTimeMillis()
        (1..3).asFlow().onEach { delay(100) }
            .flatMapMerge {
                requestFlow(it)
            }
            .map {
                println("Example of flatMapMerge: $it at ${System.currentTimeMillis() - startTime} ms from start")
            }
            .launchIn(coroutineScope)
    }

    private suspend fun exampleOfFlatMapLatestFlow(coroutineScope: CoroutineScope) {
        val startTime = System.currentTimeMillis()
        (1..3).asFlow().onEach { delay(100) }
            .flatMapLatest {
                requestFlow(it)
            }
            .map {
                println("Example of flatMapLatest: $it at ${System.currentTimeMillis() - startTime} ms from start")
            }
            .launchIn(coroutineScope)
    }


    val f1 = flow {
        val startTime = System.currentTimeMillis()
        val delayFlow: Int =
        (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms
        delay(delayFlow.toLong())
        emit(listOf(1, 2))
        println("Example of delaying coroutine and combine flow: 1 at ${System.currentTimeMillis() - startTime} ms from start")
    }

    val f2 = flow {
        val startTime = System.currentTimeMillis()
        val delayFlow: Int =
            (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms
        delay(delayFlow.toLong())
        emit(listOf(3, 4))
        println("Example of delaying coroutine and combine flow: 2 at ${System.currentTimeMillis() - startTime} ms from start")
    }

    val f3 = flow {
        val startTime = System.currentTimeMillis()
        val delayFlow: Int =
            (Random().nextInt(3) + 1) * 1000 // sleep thread for x ms
        delay(delayFlow.toLong())
        emit(listOf(5, 6))
        println("Example of delaying coroutine and combine flow: 3 at ${System.currentTimeMillis() - startTime} ms from start")
    }

    private suspend fun exampleOfSyncronFunction() {
        val nums1 = (1..3).asFlow().onEach { delay(300) }  // numbers 1..4
        val strs1 = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums1.combine(strs1) { a, b -> "$a -> $b" }
             // combine is working like that, when new flow data comes in nums1 or strs1,
             // then it will combine this flow and send to collect terminal operator
             //  compose a single string
            .collect { println("COMBINE operator: Data of two combines flows ( nums and strs ) is: " + it) }
    }

    private fun exampleOfAsynchronFunction(coroutineScope: CoroutineScope) {
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..4
        val strs = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums.zip(strs) { a, b -> "$a -> $b" }
            // zip is working like that, waiting for values from nums and strs flow,
            // and then doing something with this values
            // compose a single string
            .onEach { println("ZIP operator: Data of two combines flows ( nums and strs ) is: " + it) }
            .launchIn(coroutineScope)
    }

    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

}
