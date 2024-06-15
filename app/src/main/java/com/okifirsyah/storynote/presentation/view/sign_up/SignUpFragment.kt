package com.okifirsyah.storynote.presentation.view.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.okifirsyah.domain.utils.extensions.observeResult
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentSignUpBinding
import com.okifirsyah.storynote.utils.extension.isEmail
import com.okifirsyah.storynote.utils.extension.showLoadingDialog
import com.okifirsyah.storynote.utils.extension.showSingleActionDialog
import com.okifirsyah.storynote.utils.helper.MutableReference
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModel()

    private val loadingDialogReference = MutableReference<AlertDialog?>(null)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.apply {

            btnLogin.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
            
        }
    }

    override fun initActions() {
        binding.btnRegister.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val name = binding.edRegisterName.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val rePassword = binding.edRegisterRePassword.text.toString()

            if (email.isEmpty()) {
                binding.edRegisterEmail.error = getString(R.string.email_not_empty)
                binding.edRegisterEmail.requestFocus()
                return@setOnClickListener
            }
            if (!email.isEmail()) {
                binding.edRegisterEmail.error = getString(R.string.email_invalid)
                binding.edRegisterEmail.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                binding.edRegisterName.error = getString(R.string.name_not_empty)
                binding.edRegisterName.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.edRegisterPassword.error = getString(R.string.password_not_empty)
                binding.edRegisterPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < resources.getInteger(R.integer.min_password_length)) {
                binding.edRegisterPassword.error = getString(
                    R.string.min_password_length,
                    resources.getInteger(R.integer.min_password_length)
                )
                binding.edRegisterPassword.requestFocus()
                return@setOnClickListener
            }

            if (rePassword.isEmpty()) {
                binding.edRegisterRePassword.error = getString(R.string.re_password_not_empty)
                binding.edRegisterRePassword.requestFocus()
                return@setOnClickListener
            }

            if (rePassword.length < resources.getInteger(R.integer.min_password_length)) {
                binding.edRegisterRePassword.error = getString(
                    R.string.min_re_password_length,
                    resources.getInteger(R.integer.min_password_length)
                )
                binding.edRegisterRePassword.requestFocus()
                return@setOnClickListener
            }

            if (password != rePassword) {
                binding.edRegisterRePassword.error = getString(R.string.re_password_not_match)
                binding.edRegisterRePassword.requestFocus()

            }

            viewModel.signUp(email, password, name)

        }
    }


    override fun initObservers() {
        viewModel.signUpResult.observeResult(
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
            }
            onEmpty = {
                showLoading(false)
                showError(false, "")
            }
        }
    }

    private fun onResult(data: String) {
        Timber.d("onSuccessBind: $data")
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSuccessFragment())
    }

    override fun showLoading(isLoading: Boolean) {
        showLoadingDialog(
            loading = isLoading,
            dialogReference = loadingDialogReference
        )
    }

    override fun showError(isError: Boolean, message: String) {
        if (isError) {
            binding.edRegisterEmail.apply {
                if (message == getString(R.string.email_already_taken)) {
                    error = getString(R.string.email_already_taken)
                    requestFocus()
                    return
                }

                showSingleActionDialog(
                    title = getString(R.string.generic_error_title),
                    message = message,
                )
            }
        }

    }
}