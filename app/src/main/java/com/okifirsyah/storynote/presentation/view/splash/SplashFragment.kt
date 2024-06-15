package com.okifirsyah.storynote.presentation.view.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.okifirsyah.storynote.BuildConfig
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentSplashBinding
import com.okifirsyah.storynote.utils.constant.DurationConstant
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.tvSplashVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
    }

    override fun initProcess() {
        val handler = Handler(Looper.getMainLooper())
        val navigateRunnable = Runnable {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.isAlreadyLogin().collect { isAlreadyLogin ->
                        isAlreadyLogin?.let {
                            if (it) {
                                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToDashboardFragment())
                                return@collect
                            }
                        }
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
                        return@collect
                    }
                }
            }
        }

        handler.postDelayed(navigateRunnable, DurationConstant.DURATION_2_S)
    }

}