package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator


object Animationz {

    fun fadeInAnim(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 0.1f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }

    fun fadeOutAnim(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0.1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }

    fun slideOutRight(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X,  0f, view.width + 100f).apply {
            interpolator = AccelerateInterpolator()
            duration = 300
        }
    }

    fun slideInLeft(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -view.width - 100f, 0f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }

    fun animButton(view: View){

        val translateYAmount = (+ view.height + 150f)
        view.animate()
            .translationYBy(translateYAmount)
            .scaleXBy(1.5f)
            .scaleYBy(1.5f)
            .start()

    }

    fun slideOutRightSlideInLeft(view: View) {

        val list = mutableListOf(slideOutRight(view), slideInLeft(view))

        AnimatorSet().apply {
            playSequentially(list[0], list[1])
        }.start()

    }


}