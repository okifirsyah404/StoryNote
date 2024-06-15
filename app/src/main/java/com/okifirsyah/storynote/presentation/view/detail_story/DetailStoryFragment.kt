package com.okifirsyah.storynote.presentation.view.detail_story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.okifirsyah.data.dto.StoryDto
import com.okifirsyah.domain.utils.extensions.observeResult
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentDetailStoryBinding
import com.okifirsyah.storynote.utils.extension.formatDate
import com.okifirsyah.storynote.utils.extension.getSubAdminAreaName
import com.okifirsyah.storynote.utils.extension.gone
import com.okifirsyah.storynote.utils.extension.show
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailStoryFragment : BaseFragment<FragmentDetailStoryBinding>() {

    private val viewModel: DetailStoryViewModel by viewModel()


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailStoryBinding {
        return FragmentDetailStoryBinding.inflate(inflater, container, false)
    }

    override fun initAppBar() {
        binding.toolbar.mainToolbar.apply {
            title = "Story Detail"
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun initUI() {
    }

    override fun initProcess() {
        viewModel.getStory(arguments?.getString("storyId") ?: "")
    }

    override fun initActions() {
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                viewModel.getStory(arguments?.getString("storyId") ?: "")
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun initObservers() {
        viewModel.story.observeResult(
            viewLifecycleOwner
        ) {
            onLoading = {
                showError(false, "")
                showLoading(true)
            }
            onSuccess = { data ->
                showLoading(false)
                showError(false, "")
                onResult(data)
            }
            onError = { message ->
                showLoading(false)
                showError(true, message)
                showToast(message)
            }
            onEmpty = {
                showLoading(false)
                showError(false, "")
            }
        }
    }

    override fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                llContent.gone()
                loadingContainer.root.show()
            } else {
                loadingContainer.root.gone()
                llContent.show()
            }
        }
    }

    override fun showError(isError: Boolean, message: String) {
        binding.apply {

            errorContainer.btnRetry.setOnClickListener {
                viewModel.getStory(arguments?.getString("storyId") ?: "")
            }

            if (isError) {
                llContent.gone()
                errorContainer.tvErrorMessage.text = message
                errorContainer.root.show()
            } else {
                errorContainer.root.gone()
                llContent.show()
            }
        }
    }


    private fun onResult(data: StoryDto) {
        binding.apply {
            tvDetailName.text = getString(R.string.author_posted, data.name)
            tvDetailDescription.text = data.description
            tvCreateAt.text = data.createdAt.formatDate()

            ivDetailPhoto.load(data.photoUrl) {
                placeholder(R.drawable.placehoder)
            }

            if (data.latitude != null && data.longitude != null) {
                lifecycleScope.launch {
                    chipLoc.text = requireActivity().getSubAdminAreaName(
                        data.latitude!!.toDouble(),
                        data.longitude!!.toDouble()
                    )
                }
                chipLoc.show()
            } else {
                chipLoc.gone()
            }
        }
    }


}