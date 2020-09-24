package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
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

    //REMOVE AND CHANGE TO VIEW. ABOVE
    fun fadeInAnim(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 0.1f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }
    //  REMOVE AND CHANGE TO VIEW. ABOVE
    fun fadeOutAnim(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0.1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }
    // CHANGE TO VIEW.
    fun slideOutRight(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, view.width + 100f).apply {
            interpolator = AccelerateInterpolator()
            duration = 300
        }
    }

    // CHANGE TO VIEW.
    fun slideInLeft(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -view.width - 100f, 0f).apply {
            interpolator = DecelerateInterpolator()
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
            playSequentially(slideOutRight(v), slideInLeft(v))
        }

    }

    fun AppCompatTextView.slideOutRightInLeftSetText(sText: String): AnimatorSet {

        val anim1 = slideOutRight(this)
        val anim2 = slideInLeft(this)

        anim1.apply {
            duration = flipCardDurationOneThird
        }.doOnEnd { _ ->
            this.text = sText
        }
        anim2.apply { duration = flipCardDurationOneThird }

        return AnimatorSet().apply {
            playSequentially(anim1, anim2)
        }

    }

    fun AppCompatTextView.flipNewRound(context: Context, sText: String): AnimatorSet {

        val v = this

        val scale: Float = context.resources.displayMetrics.density * 8000 // MOVE
        checkCameraDistance(this, scale) // MOVE

        val a1 = flipToBackY().apply {
            duration = flipCardDurationOneHalf
            doOnEnd {
                v.text = sText
            }
        }
        val a2 = flipToFrontY().apply {
            duration = flipCardDurationOneHalf
        }

        return AnimatorSet().apply {
            playSequentially(a1, a2)
        }

    }

    fun checkCameraDistance(view: View, scale: Float) {
        when (view.cameraDistance) {
            scale -> { }
            else -> {
                view.cameraDistance = scale
            }

        }
    }


}