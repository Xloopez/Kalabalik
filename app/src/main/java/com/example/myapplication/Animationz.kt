package com.example.myapplication

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator

object Animationz {

    fun fadeInAnim(view: View){

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
        view.animation = fadeIn
        fadeIn.start()

    }

    fun fadeOutAnim(view: View){
        val fadeIOut = AlphaAnimation(1f, 0f)
        fadeIOut.apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
        view.animation = fadeIOut
        fadeIOut.start()
    }

    fun animButton(view: View){

        val translateYAmount = (+ view.height + 150f)
        view.animate()
            .translationYBy(translateYAmount)
            .scaleXBy(1.5f)
            .scaleYBy(1.5f)
            .start()

    }


}