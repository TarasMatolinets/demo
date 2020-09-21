package com.demo.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.demo.demo.BR
import com.demo.demo.R
import com.demo.demo.databinding.FooterItemBinding
import com.demo.demo.databinding.HorizontalItemBinding
import com.demo.demo.databinding.VerticalItemBinding
import com.demo.mvvm.adapter.AdapterPosition.HORIZONTAL
import com.demo.mvvm.adapter.AdapterPosition.VERTICAL
import com.demo.mvvm.model.GameModelUI
import com.demo.networking.Failure
import com.demo.utils.load


const val GAME_TRANSITION_IMAGE = "details_image"
const val GAME_TRANSITION_TITLE = "details_title"

class GameAdapter constructor(
    private val adapterPosition: AdapterPosition,
    private val itemClickCallback: (GameInfo) -> Unit
) : PagedListAdapter<GameModelUI, ViewHolder>(diffCallback) {
    private var state: State = State.Loading

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == NORMAL_ITEM) {
            when (adapterPosition) {
                HORIZONTAL -> GameHorizontalViewHolder(prepareHorizontalItemBinding(layoutInflater, parent))
                VERTICAL -> GameVerticalViewHolder(prepareVerticalItemBinding(layoutInflater, parent))
            }
        } else {
            LoadingViewHolder(prepareFooterItemBinding(layoutInflater, parent))
        }
    }

    private fun prepareFooterItemBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): FooterItemBinding {
        return DataBindingUtil.inflate(
            layoutInflater,
            R.layout.footer_item,
            parent,
            false
        )
    }

    private fun prepareVerticalItemBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): VerticalItemBinding {
        val verticalBinding = DataBindingUtil.inflate<VerticalItemBinding>(
            layoutInflater,
            R.layout.vertical_item,
            parent,
            false
        )
        verticalBinding.ivImage.transitionName = GAME_TRANSITION_IMAGE
        verticalBinding.tvTitle.transitionName = GAME_TRANSITION_TITLE

        verticalBinding.gameItemCallback = object : GameItemClick {
            override fun gameItemClick(selectedItem: GameModelUI) {
                itemClickCallback(GameInfo(verticalBinding.clGame, verticalBinding.tvTitle, selectedItem))
            }
        }
        return verticalBinding
    }

    private fun prepareHorizontalItemBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): HorizontalItemBinding {
        val horizontalBinding = DataBindingUtil.inflate<HorizontalItemBinding>(
            layoutInflater,
            R.layout.horizontal_item,
            parent,
            false
        )
        horizontalBinding.ivImage.transitionName = GAME_TRANSITION_IMAGE
        horizontalBinding.tvTitle.transitionName = GAME_TRANSITION_TITLE
        horizontalBinding.gameItemCallback = object : GameItemClick {
            override fun gameItemClick(selectedItem: GameModelUI) {
                itemClickCallback(GameInfo(horizontalBinding.clGame, horizontalBinding.tvTitle, selectedItem))
            }
        }
        return horizontalBinding
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) NORMAL_ITEM else FOOTER_ITEM
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.Loading || state == State.Error())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == NORMAL_ITEM) {
            when (holder) {
                is GameHorizontalViewHolder -> holder.bind(getItem(position))
                is GameVerticalViewHolder -> holder.bind(getItem(position))
            }
        } else {
            (holder as LoadingViewHolder).bind(state)
        }
    }

    companion object {
        const val NORMAL_ITEM = 1
        const val FOOTER_ITEM = 2
        var diffCallback: DiffUtil.ItemCallback<GameModelUI> = object : DiffUtil.ItemCallback<GameModelUI>() {
            override fun areItemsTheSame(oldItem: GameModelUI, newItem: GameModelUI): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GameModelUI, newItem: GameModelUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class GameHorizontalViewHolder(private val binding: HorizontalItemBinding) : ViewHolder(binding.root) {

    fun bind(gameModelUI: GameModelUI?) {
        gameModelUI?.let { gameUI ->
            with(binding) {
                binding.setVariable(BR.gameUI, gameUI)
                executePendingBindings()
                gameUI.backgroundImage?.let {
                    binding.ivImage.load(gameUI.backgroundImage)
                }
            }
        }
    }
}

class GameVerticalViewHolder(private val binding: VerticalItemBinding) : ViewHolder(binding.root) {
    fun bind(gameModelUI: GameModelUI?) {
        gameModelUI?.let { gameUI ->
            with(binding) {
                binding.setVariable(BR.gameUI, gameUI)
                executePendingBindings()
                gameUI.backgroundImage?.let {
                    binding.ivImage.load(gameUI.backgroundImage)
                }
            }
        }
    }
}

class LoadingViewHolder(private val binding: FooterItemBinding) : ViewHolder(binding.root) {
    fun bind(status: State) {
        with(binding) {
            executePendingBindings()
            progressBar.visibility = if (status == State.Loading) View.VISIBLE else View.INVISIBLE
        }
    }
}

enum class AdapterPosition {
    HORIZONTAL, VERTICAL
}

sealed class State {
    object Done : State()
    object Loading : State()
    class Error(val failure: Failure? = null) : State()
}

interface GameItemClick {
    fun gameItemClick(selectedItem: GameModelUI)
}

data class GameInfo(val viewImage: View, val viewTitle: View, val gameUI: GameModelUI)

