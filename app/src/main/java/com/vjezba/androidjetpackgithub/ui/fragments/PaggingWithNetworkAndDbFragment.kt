package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vjezba.androidjetpackgithub.databinding.FragmentPaggingNetworkAndDbBinding
import com.vjezba.androidjetpackgithub.ui.activities.LanguagesActivity
import com.vjezba.androidjetpackgithub.ui.dialog.ChooseProgrammingLanguageDialog
import com.vjezba.androidjetpackgithub.viewmodels.PaggingWithNetworkAndDbViewModel
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_pagging_network_and_db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val UPDATE_PERIOD = 10000L

class PaggingWithNetworkAndDbFragment : Fragment() {

    private val viewModel : PaggingWithNetworkAndDbViewModel by viewModel()

    private var automaticIncreaseNumberByOne: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPaggingNetworkAndDbBinding.inflate(inflater, container, false)
        context ?: return binding.root

        viewModel.incrementNumberAutomaticallyByOne.observe(viewLifecycleOwner, Observer { currentNumber ->
            tvNumberIncreaseAutomatically.text = "" + currentNumber
        })

        viewModel.incrementNumberManuallyByOne.observe(viewLifecycleOwner, Observer { currentNumber ->
            tvNumberIncreaseManually.text = "" + currentNumber
        })


        activity?.speedDial?.visibility = View.GONE
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        btnIncreaseNumber.setOnClickListener {
            viewModel.incrementManuallyByOne()
        }

        btnChooseLanguage.setOnClickListener {
            val chooseProgrammingLanguageDialog =
                ChooseProgrammingLanguageDialog(automaticIncreaseNumberByOne)
            chooseProgrammingLanguageDialog.show(
                (requireActivity() as LanguagesActivity).supportFragmentManager,
                "")
        }

        automaticIncreaseNumberByOne?.cancel()
        automaticIncreaseNumberByOne = lifecycleScope.launch {

            (1..3).asFlow().map { requestFlow(it) }
                .onEach { stringData -> println("Data of flow is: " + stringData) }
                .collect()

            // launchIn means, we are collecting data asinkron
            // collect, collectLatest and so on.. means, we are collecting data sinkrono

            exampleOfAsynchronFunction(this)// this is asincron function because of operator launchIn
            exampleOfSyncronFunction() // this is sincrono function because of operator collect

            println("Only when this this two above function 'exampleOfAsynchronFunction' and 'exampleOfSyncronFunction'" +
                    "  are done, then only it will be executed this line" +
                    "\n Because this function 'exampleOfSyncronFunction' is suspending function of, because of operator 'collect'  ")

            while (true) {
                delay(UPDATE_PERIOD)
                try {
                    handleUpdate()
                } catch (ex: Exception) {
                    Log.v("ERROR","Periodic remote-update failed...", ex)
                }
            }
        }

    }

    private suspend fun exampleOfSyncronFunction() {
        val nums1 = (1..3).asFlow().onEach { delay(300) }  // numbers 1..4
        val strs1 = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums1.combine(strs1) { a, b -> "$a -> $b" } // compose a single string
            .collect { println( "COMBINE operator: Data of two combines flows ( nums and strs ) is: " + it ) }
    }

    private fun exampleOfAsynchronFunction( coroutineScope: CoroutineScope) {
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..4
        val strs = flowOf("one", "two", "three").onEach { delay(400) }  // strings
        nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string
            .onEach { println( "ZIP operator: Data of two combines flows ( nums and strs ) is: " + it ) }
            .launchIn(coroutineScope)
    }

    fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

    private fun handleUpdate() {
        viewModel.incrementAutomaticallyByOne()
    }

}