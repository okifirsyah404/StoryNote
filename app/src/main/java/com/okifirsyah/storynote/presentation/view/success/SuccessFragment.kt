package com.okifirsyah.storynote.presentation.view.success

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.navigation.fragment.findNavController
import com.okifirsyah.storynote.R
import com.okifirsyah.storynote.base.BaseFragment
import com.okifirsyah.storynote.databinding.FragmentSuccessBinding
import com.okifirsyah.storynote.utils.constant.DurationConstant
import timber.log.Timber
import kotlin.random.Random


class SuccessFragment : BaseFragment<FragmentSuccessBinding>() {

    private lateinit var thumbAlphaAnimatorSet: AnimatorSet
    private lateinit var glintAnimatorSet: AnimatorSet

    private lateinit var timer: CountDownTimer

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSuccessBinding {
        return FragmentSuccessBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        playAnimation()

        timer =
            object : CountDownTimer(DurationConstant.DURATION_5_S, DurationConstant.DURATION_1_S) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / DurationConstant.DURATION_1_S

                    binding.tvCountDown.text = getString(R.string.desc_count_success, seconds)

                    Timber.d("onTick: $seconds")
                }

                override fun onFinish() {
                    findNavController().navigate(SuccessFragmentDirections.actionSuccessFragmentToSignInFragment())
                }
            }

        timer.start()

    }

    override fun onClose() {
        super.onClose()
        thumbAlphaAnimatorSet.cancel()
        glintAnimatorSet.cancel()
        timer.cancel()
        Timber.d("onDestroy: timer canceled")
    }

    private fun playAnimation() {
        val thumbAlphaAnimator = ObjectAnimator.ofFloat(binding.ivSuccess, "alpha", 0f, 1f)
        thumbAlphaAnimator.duration = DurationConstant.DURATION_1_S

        val thumbScaleXAnimator = ObjectAnimator.ofFloat(binding.ivSuccess, "scaleX", 0f, 1.5f, 1f)
        thumbScaleXAnimator.duration = DurationConstant.DURATION_1_S
        val thumbScaleYAnimator = ObjectAnimator.ofFloat(binding.ivSuccess, "scaleY", 0f, 1.5f, 1f)
        thumbScaleYAnimator.duration = DurationConstant.DURATION_1_S

        thumbAlphaAnimatorSet = AnimatorSet()
        thumbAlphaAnimatorSet.playTogether(
            thumbAlphaAnimator,
            thumbScaleXAnimator,
            thumbScaleYAnimator
        )
        thumbAlphaAnimatorSet.interpolator = AccelerateDecelerateInterpolator()

        thumbAlphaAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                playGlintAnimations()
            }
        })

        thumbAlphaAnimatorSet.start()
    }

    private fun playGlintAnimations() {
        val randomDuration = { min: Long, max: Long -> Random.nextLong(min, max) }

        val firstGlintAlphaAnimator =
            ObjectAnimator.ofFloat(binding.ivGlint1, "alpha", 0f, 1f).apply {
                duration =
                    randomDuration(DurationConstant.DURATION_0_8_S, DurationConstant.DURATION_2_S)
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        val secondGlintAlphaAnimator =
            ObjectAnimator.ofFloat(binding.ivGlint2, "alpha", 0f, 1f).apply {
                duration =
                    randomDuration(DurationConstant.DURATION_0_8_S, DurationConstant.DURATION_2_S)
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        val thirdGlintAlphaAnimator =
            ObjectAnimator.ofFloat(binding.ivGlint3, "alpha", 0f, 1f).apply {
                duration =
                    randomDuration(DurationConstant.DURATION_0_8_S, DurationConstant.DURATION_2_S)
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        val fourthGlintAlphaAnimator =
            ObjectAnimator.ofFloat(binding.ivGlint4, "alpha", 0f, 1f).apply {
                duration =
                    randomDuration(DurationConstant.DURATION_0_8_S, DurationConstant.DURATION_2_S)
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        glintAnimatorSet = AnimatorSet()

        glintAnimatorSet.playTogether(
            firstGlintAlphaAnimator,
            secondGlintAlphaAnimator,
            thirdGlintAlphaAnimator,
            fourthGlintAlphaAnimator
        )

        glintAnimatorSet.start()
    }
}