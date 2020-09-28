package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd


object Animationz {

    private const val flipCardDuration = 1200L
    private const val flipCardDurationOneHalf = flipCardDuration/2
    private const val flipCardDurationOneThird = flipCardDuration/3
    const val flipCardDurationOneFourth = flipCardDuration/4

    fun View.flipToBackY(): ObjectAnimator { return ObjectAnimator.ofFloat(this, View.ROTATION_Y, 0f, 90f)}
    fun View.flipToFrontY(): ObjectAnimator { return ObjectAnimator.ofFloat(this, View.ROTATION_Y, -90f, 0f)}

    fun View.fadeInAnim1(): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, View.ALPHA, 0.1f, 1f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 300 }
    }
    fun View.fadeOutAnim1(): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0.1f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 300 }
    }

    fun View.slideOutRight(): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, this.width + 300f).apply {
            interpolator = AccelerateInterpolator()
            duration = 300
        }
    }
    fun View.slideInLeft(): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, View.TRANSLATION_X, -this.width -300f, 0f).apply {
            interpolator = AccelerateInterpolator()
            duration = 300
        }
    }

    fun hideSoftKeyBoard(activity: Activity, view: View){
        val im = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(view.windowToken,0)
    }

    fun View.slideOutRightSlideInLeft(): AnimatorSet {
        val v = this
        return AnimatorSet().apply {
            playSequentially(v.slideOutRight(), v.slideInLeft())
        }
    }

    fun AppCompatTextView.slideOutRightInLeftSetText(sText: String): AnimatorSet {

        val v = this
        val anim1 = v.slideOutRight()
        val anim2 = v.slideInLeft()

        anim1.apply {
            duration = flipCardDurationOneThird
        }.doOnEnd { _ ->
            v.text = sText
        }
        anim2.apply { duration = flipCardDurationOneThird }

        return AnimatorSet().apply {
            playSequentially(anim1, anim2)
        }

    }

    //TODO to View.
    fun View.checkCameraDistance(targetScale: Float) {
        when (this.cameraDistance) {
            targetScale -> { }
            else -> {
                this.cameraDistance = targetScale
            }

        }
    }


}