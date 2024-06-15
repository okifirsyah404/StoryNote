package com.okifirsyah.customview.textinput

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.okifirsyah.customview.R

open class MainEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), View.OnTouchListener {

    var prefixIconDrawable: Drawable? = null
    var suffixIconDrawable: Drawable? = null

    private var backgroundColor: Int = Color.WHITE
    private var cornerRadius = 30f
    private var horizontalPadding = 16f
    private var prefixIconPadding = 16f
    private var suffixIconPadding = 16f
    private var outlineThickness = 4f
    private val additionalVerticalPadding = 8f
    private var onSuffixIconTapListener: ((View) -> Unit)? = null

    private var outlineColor: Int = Color.GRAY
    private var focusOutlineColor: Int = Color.BLUE
    private var isFocusedState = false

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MainEditText, 0, 0).apply {
            try {
                prefixIconDrawable = getDrawable(R.styleable.MainEditText_prefixIcon)
                suffixIconDrawable = getDrawable(R.styleable.MainEditText_suffixIcon)
                cornerRadius = getDimension(R.styleable.MainEditText_borderRadius, cornerRadius)
                backgroundColor =
                    getColor(R.styleable.MainEditText_backgroundColor, Color.TRANSPARENT)
                outlineColor = getColor(R.styleable.MainEditText_outlineColor, Color.GRAY)
                focusOutlineColor = getColor(R.styleable.MainEditText_focusOutlineColor, Color.BLUE)
                outlineThickness =
                    getDimension(R.styleable.MainEditText_outlineThickness, outlineThickness)
                setPadding(
                    (horizontalPadding + prefixIconPadding).toInt(),
                    paddingTop + additionalVerticalPadding.toInt(),
                    (horizontalPadding + suffixIconPadding).toInt(),
                    paddingBottom + additionalVerticalPadding.toInt()
                )
                setOnTouchListener(this@MainEditText)
            } finally {
                recycle()
            }
        }

        setBackgroundActive(isFocusedState)
        setDrawableTint(isFocusedState)

        paint.color = backgroundColor

        setEditTextCompoundDrawables(prefixIconDrawable, suffixIconDrawable)

        initialize()
    }

    private fun initialize() {
        setOnFocusChangeListener { _, hasFocus ->
            isFocusedState = hasFocus
            setDrawableTint(hasFocus)
            setBackgroundActive(hasFocus)
            invalidate()
        }
    }

    private fun setDrawableTint(isFocusedState: Boolean) {
        prefixIconDrawable?.setTint(if (isFocusedState) focusOutlineColor else outlineColor)
        suffixIconDrawable?.setTint(if (isFocusedState) focusOutlineColor else outlineColor)
    }

    private fun setBackgroundActive(isActive: Boolean) {
        background = if (isActive) {
            ResourcesCompat.getDrawable(resources, R.drawable.active_edit_text_background, null)
        } else {
            ResourcesCompat.getDrawable(resources, R.drawable.edit_text_background, null)
        }
    }

    fun setEditTextCompoundDrawables(
        prefixIcon: Drawable? = null,
        suffixIcon: Drawable? = null
    ) {
        compoundDrawablePadding = prefixIconPadding.toInt()
        setCompoundDrawablesWithIntrinsicBounds(prefixIcon, null, suffixIcon, null)
        invalidate()
    }

    fun setOnSuffixIconTapListener(listener: (View) -> Unit) {
        onSuffixIconTapListener = listener
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null && onSuffixIconTapListener != null) {

            var isButtonClicked = false
            val buttonPosition: Float

            if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                buttonPosition = prefixIconPadding
                when {
                    event.x < buttonPosition -> isButtonClicked = true

                }
            } else {
                buttonPosition = width - suffixIconPadding - suffixIconDrawable!!.intrinsicWidth
                when {
                    event.x > buttonPosition -> isButtonClicked = true
                }
            }

            if (isButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        suffixIconDrawable?.let {
                            it.setTint(focusOutlineColor)
                            onSuffixIconTapListener?.invoke(v)
                            setDrawableTint(isFocusedState)
                            invalidate()
                        }
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        suffixIconDrawable?.let {
                            it.setTint(outlineColor)
                            setDrawableTint(isFocusedState)
                            invalidate()
                        }
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return super.onTouchEvent(event)
    }
}