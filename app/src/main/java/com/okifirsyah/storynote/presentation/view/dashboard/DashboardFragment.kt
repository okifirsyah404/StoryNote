package com.okifirsyah.storynote.presentation.view.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.okifirsyah.customview.recyclerview.decorator.ListRecyclerViewItemDivider
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentDashboardBinding
import com.okifirsyah.storynote.presentation.adapter.LoadingStateAdapter
import com.okifirsyah.storynote.presentation.adapter.StoryAdapter
import com.okifirsyah.storynote.presentation.adapter.StoryPagingAdapter
import com.okifirsyah.storynote.utils.extension.gone
import com.okifirsyah.storynote.utils.extension.hide
import com.okifirsyah.storynote.utils.extension.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val viewModel: DashboardViewModel by viewModel()
    private val storyAdapter by lazy {
        StoryAdapter(
            onClick = {
                navigateToDetailStoryFragment(it)
            }
        )
    }

    private val storyPagingAdapter: StoryPagingAdapter by lazy {
        StoryPagingAdapter(requireActivity()) {
            navigateToDetailStoryFragment(it)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }

    override fun initAppBar() {
        binding.toolbar.apply {
            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_settingFragment)
            }

            btnMap.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_storyMapsFragment)
            }
        }
    }

    override fun initUI() {
        binding.rvStory.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addItemDecoration(
                ListRecyclerViewItemDivider(
                    resources.getDimension(R.dimen.dimen_16dp).toInt(),
                    resources.getDimension(R.dimen.dimen_16dp).toInt()
                )
            )

            adapter = storyPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyPagingAdapter.retry()
                }
            )

            isNestedScrollingEnabled = false
        }
    }

    override fun initActions() {
        binding.apply {
            layoutRefresh
                .setOnRefreshListener {
                    initObservers()
                    binding.rvStory.scrollToPosition(0)
                    layoutRefresh.isRefreshing = false
                }

            fabAddStory.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_addStoryFragment)
            }
        }
    }


    override fun initObservers() {
        viewModel.getPagingStories().observe(this) {
            storyPagingAdapter.submitData(lifecycle, it)
        }

        storyPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                if (storyPagingAdapter.itemCount < 1) {
                    showError(false, "")
                    showEmpty(true)
                }
            }
            if (view == null) return@addLoadStateListener
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    showEmpty(false)
                    showError(false, "")
                    showLoading(true)
                }

                is LoadState.NotLoading -> {
                    showError(false, "")
                    showEmpty(false)
                    showLoading(false)
                    binding.rvStory.scheduleLayoutAnimation()
                }

                is LoadState.Error -> {
                    showLoading(false)
                    showEmpty(false)
                    showError(true, (loadState.refresh as LoadState.Error).error.message.toString())
                }

                else -> {
                    showLoading(false)
                    showError(false, "")
                    showEmpty(false)
                }
            }
        }
    }

    override fun showError(isError: Boolean, message: String) {

        if (isError) {
            binding.rvStory.hide()
            binding.errorContainer.root.show()
            binding.errorContainer.tvErrorMessage.text = message
            binding.errorContainer.btnRetry.setOnClickListener {
                viewModel.getPagingStories()
            }
        } else {
            binding.errorContainer.root.gone()
            binding.rvStory.show()
        }

    }

    override fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.rvStory.hide()
            binding.loadingContainer.root.show()
        } else {
            binding.loadingContainer.root.gone()
            binding.rvStory.show()
        }

    }

    override fun showEmpty(isEmpty: Boolean) {

        if (isEmpty) {
            binding.rvStory.hide()
            binding.emptyContainer.root.show()
        } else {
            binding.emptyContainer.root.gone()
            binding.rvStory.hide()
        }

    }

    private fun navigateToDetailStoryFragment(id: String) {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToDetailStoryFragment(
                id
            )
        )
    }

    companion object {
        const val SHOULD_REFRESH = "SHOULD_REFRESH"
    }

}