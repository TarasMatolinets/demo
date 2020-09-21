package com.demo.mvvm.view.activity

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.TransitionSet
import androidx.activity.viewModels
import androidx.core.transition.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.demo.demo.R
import com.demo.demo.databinding.ActivityGameDetailsBinding
import com.demo.mvvm.adapter.GAME_TRANSITION_IMAGE
import com.demo.mvvm.adapter.GAME_TRANSITION_TITLE
import com.demo.mvvm.model.GameModelUI
import com.demo.mvvm.viewModel.GameDetailsActivityViewModel
import com.demo.utils.load
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class GameDetailsActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: GameDetailsActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityGameDetailsBinding>(this, R.layout.activity_game_details)

        binding.viewModel = viewModel

        intent.extras?.let { bundle ->
            val gameModel: GameModelUI = bundle.get(ITEMS_EXTRA) as GameModelUI
            viewModel.rating.set(gameModel.rating.toInt())
            viewModel.title.set(gameModel.name)

            gameModel.backgroundImage?.let { url ->
                binding.ivImage.load(gameModel.backgroundImage) {
                    supportStartPostponedEnterTransition()
                }
                window.sharedElementEnterTransition = TransitionSet()
                    .addTransition(ChangeImageTransform())
                    .addTransition(ChangeBounds())
                    .apply {
                        doOnEnd {
                            binding.ivImage.load(url)
                        }
                    }
            }
        }
        window.enterTransition = Fade().apply {
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            excludeTarget(R.id.action_bar_container, true)
        }

        binding.ivImage.transitionName = GAME_TRANSITION_IMAGE
        binding.tvTitle.transitionName = GAME_TRANSITION_TITLE
    }

    companion object {
        private const val ITEMS_EXTRA = "ITEMS_EXTRA"

        fun getBundle(gameModel: GameModelUI) = Bundle().apply {
            putParcelable(ITEMS_EXTRA, gameModel)
        }
    }
}