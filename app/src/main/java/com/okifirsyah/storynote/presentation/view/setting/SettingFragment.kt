package com.okifirsyah.storynote.presentation.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentSettingBinding
import com.okifirsyah.storynote.utils.constant.LocaleConstant
import com.okifirsyah.storynote.utils.extension.showDecisionDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    private val viewModel: SettingViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }

    override fun initAppBar() {
        binding.toolbar.mainToolbar.apply {
            title = getString(R.string.setting)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun initUI() {

    }

    override fun initProcess() {

    }

    override fun initObservers() {
        viewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean? ->
            binding.apply {
                when (isDarkModeActive) {
                    true -> btnDarkTheme.isChecked = true
                    false -> btnLightTheme.isChecked = true
                    null -> btnSystemTheme.isChecked = true
                }
            }
        }

        viewModel.getLocaleSettings().observe(
            this
        ) { locale: String? ->
            binding.apply {
                when (locale) {
                    LocaleConstant.ID -> btnIdLanguage.isChecked = true
                    LocaleConstant.EN -> btnUsLanguage.isChecked = true
                    null -> btnUsLanguage.isChecked = true
                }
            }
        }
    }


    override fun initActions() {
        binding.apply {
            btnSystemTheme.setOnClickListener {
                Timber.tag("SettingFragment").d("Change Theme to System")
                viewModel.clearThemeSetting()
                Timber.tag("SettingFragment").d("Theme setting cleared")
            }
            btnDarkTheme.setOnClickListener {
                viewModel.saveThemeSetting(true)
            }
            btnLightTheme.setOnClickListener {
                viewModel.saveThemeSetting(false)
            }
            btnIdLanguage.setOnClickListener {
                viewModel.saveLocaleSetting(LocaleConstant.ID)
            }
            btnUsLanguage.setOnClickListener {
                viewModel.saveLocaleSetting(LocaleConstant.EN)
            }
            btnSignOut.setOnClickListener {
                showDecisionDialog(
                    title = getString(R.string.sign_out),
                    message = getString(R.string.dialog_desc_sign_out),
                    onYes = {
                        viewModel.deleteAccessToken()
                        findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToSignInFragment())
                    }
                )
            }
        }
    }

}