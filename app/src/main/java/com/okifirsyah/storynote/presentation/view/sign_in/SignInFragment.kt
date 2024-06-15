package com.okifirsyah.storynote.presentation.view.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.okifirsyah.domain.utils.extensions.observeResult
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentSignInBinding
import com.okifirsyah.storynote.utils.extension.isEmail
import com.okifirsyah.storynote.utils.extension.showLoadingDialog
import com.okifirsyah.storynote.utils.extension.showSingleActionDialog
import com.okifirsyah.storynote.utils.helper.MutableReference
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    private val viewModel: SignInViewModel by viewModel()

    private val loadingDialogReference = MutableReference<AlertDialog?>(null)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignInBinding {
        return FragmentSignInBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
    }


    override fun initActions() {


        binding.btnRegister.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    override fun initProcess() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty()) {
                binding.edLoginEmail.error = getString(R.string.email_not_empty)
                binding.edLoginEmail.requestFocus()
                return@setOnClickListener
            }

            if (!email.isEmail()) {
                binding.edLoginEmail.error = getString(R.string.email_invalid)
                binding.edLoginEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edLoginPassword.error = getString(R.string.password_not_empty)
                binding.edLoginPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < resources.getInteger(R.integer.min_password_length)) {
                binding.edLoginPassword.error = getString(
                    R.string.min_password_length,
                    resources.getInteger(R.integer.min_password_length)
                )
                binding.edLoginPassword.requestFocus()
                return@setOnClickListener
            }

            viewModel.signIn(email, password)
        }
    }

    override fun initObservers() {
        viewModel.signInResult.observeResult(
            viewLifecycleOwner
        ) {
            onLoading = {
                showError(false, "")
                showLoading(true)
            }
            onSuccess = { _ ->
                showLoading(false)
                onResult()
            }
            onError = { message ->
                showLoading(false)
                showError(true, message)
            }
            onEmpty = {
                showLoading(false)
                showError(false, "")
            }
        }
    }

    override fun showLoading(isLoading: Boolean) {
        showLoadingDialog(
            loading = isLoading,
            dialogReference = loadingDialogReference
        )
    }

    private fun onResult() {
        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToDashboardFragment())
    }

    override fun showError(isError: Boolean, message: String) {
        if (isError) {
            if (message == getString(R.string.code_user_not_found)) {
                binding.edLoginEmail.apply {
                    error = getString(R.string.user_not_found)
                    requestFocus()
                    return
                }
            }

            if (message == getString(R.string.code_invalid_password)) {
                binding.edLoginPassword.apply {
                    error = getString(R.string.invalid_password)
                    requestFocus()
                    return
                }
            }

            showSingleActionDialog(
                title = getString(R.string.generic_error_title),
                message = message,
            )
        }
    }
}