package com.okifirsyah.customview.textinput

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import com.okifirsyah.customview.R
import com.okifirsyah.customview.utils.enums.PasswordType

class MainPasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : MainEditText(context, attrs, defStyleAttr) {

    private var onPasswordShowIcon: Drawable? = null
    private var onPasswordHideIcon: Drawable? = null
    private var isPasswordVisible: Boolean = false
    private var beforeTextChangeValidator: ((CharSequence, Int, Int, Int) -> Unit)? = null
    private var onTextChangeValidator: ((CharSequence, Int, Int, Int) -> Unit)? = null
    private var afterTextChangeValidator: ((Editable) -> Unit)? = null
    private var passwordType: PasswordType = PasswordType.MAIN

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MainPasswordEditText, 0, 0).apply {
            try {
                onPasswordShowIcon =
                    getDrawable(R.styleable.MainPasswordEditText_onPasswordShowIcon)
                onPasswordHideIcon =
                    getDrawable(R.styleable.MainPasswordEditText_onPasswordHideIcon)
                passwordType =
                    PasswordType.from(getInt(R.styleable.MainPasswordEditText_passwordType, 0))
            } finally {
                recycle()
            }
        }

        setSuffixDrawable()

        inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()

        setOnSuffixIconTapListener {
            togglePasswordVisibility()
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validatePassword(s)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun validatePassword(s: CharSequence) {
        val minPassLength = resources.getInteger(R.integer.min_password_length)

        when (passwordType) {
            PasswordType.MAIN -> {
                if (s.isEmpty()) {
                    error = resources.getString(R.string.password_not_empty)
                    requestFocus()
                } else if (s.length < minPassLength) {
                    error =
                        resources.getString(R.string.min_password_length, minPassLength)
                    requestFocus()
                } else {
                    error = null
                }
            }

            PasswordType.CONFIRMATION -> {
                if (s.isEmpty()) {
                    error = resources.getString(R.string.re_password_not_empty)
                    requestFocus()
                } else if (s.length < minPassLength) {
                    error =
                        resources.getString(R.string.min_re_password_length, minPassLength)
                    requestFocus()
                } else {
                    error = null
                }
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) {
            null
        } else {
            PasswordTransformationMethod.getInstance()
        }
        setSuffixDrawable()
        setSelection(text?.length ?: 0)
        invalidate()
    }

    private fun setSuffixDrawable() {
        suffixIconDrawable = if (isPasswordVisible) {
            onPasswordShowIcon
        } else {
            onPasswordHideIcon
        }
        setEditTextCompoundDrawables(prefixIconDrawable, suffixIconDrawable)
    }

}