package com.example.myapplication

import android.view.View

object Util {

    fun setViewVisibilityFadeInOut(view: View, visible: Boolean) {
        when (visible){
            true -> {
                view.visibility = View.VISIBLE
                Animationz.fadeInAnim(view)
            }
            false -> {
                view.visibility = View.INVISIBLE
                Animationz.fadeOutAnim(view)
            }
        }
    }

}