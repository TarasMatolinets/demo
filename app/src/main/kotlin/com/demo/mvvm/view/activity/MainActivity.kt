package com.demo.mvvm.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.demo.demo.BuildConfig
import com.demo.demo.R
import com.demo.demo.databinding.ActivityMainBinding
import com.demo.mvvm.adapter.GAME_TRANSITION_IMAGE
import com.demo.mvvm.adapter.GAME_TRANSITION_TITLE
import com.demo.mvvm.adapter.State
import com.demo.mvvm.viewModel.MainActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


const val TAG = "com.demo.DEMO"

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = viewModel

        handleViewModelStates()
    }

    private fun handleViewModelStates() {
        viewModel.failure.observe(this, {
            Toast.makeText(this, getText(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
        })

        viewModel.createdGameList.observe(this, {
            viewModel.adapterCreated.submitList(it)
        })

        viewModel.releasedGameList.observe(this, {
            viewModel.adapterReleased.submitList(it)
        })

        viewModel.ratingGameList.observe(this, {
            viewModel.adapterMostRated.submitList(it)
        })

        viewModel.getCreatedState().observe(this, {
            checkStates(it)
            if (viewModel.adapterCreated.currentList?.size != 0) {
                viewModel.adapterCreated.setState(it)
            }
        })
        viewModel.getReleasedState().observe(this, {
            checkStates(it)
            if (viewModel.adapterReleased.currentList?.size != 0) {
                viewModel.adapterReleased.setState(it)
            }
        })
        viewModel.getRatingState().observe(this, {
            checkStates(it)
            if (viewModel.adapterMostRated.currentList?.size != 0) {
                viewModel.adapterMostRated.setState(it)
            }
        })

        viewModel.gameClicked.observe(this, {
            val result = it.getContent()

            result?.let { triple ->
                val imagePair = Pair(triple.viewImage, GAME_TRANSITION_IMAGE)
                val titlePair = Pair(triple.viewTitle, GAME_TRANSITION_TITLE)

                val intent = Intent(this, GameDetailsActivity::class.java)
                intent.putExtras(GameDetailsActivity.getBundle(triple.gameUI))
                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, imagePair, titlePair)
                startActivity(intent, options.toBundle())
            }
        })
    }

    private fun checkStates(it: State?) {
        when (it) {
            State.Done,
            State.Loading -> {
                viewModel.actionButton.set(false)
                viewModel.progress.set(false)
            }
            is State.Error -> {
                viewModel.actionButton.set(true)
                viewModel.progress.set(false)

                val error = it.failure
                if (BuildConfig.DEBUG) {
                    error?.let { Log.e(TAG, it.errorMessage ?: "Something went wrong") }
                }
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        }
    }
}