package com.example.myapplication

import android.view.View
import java.lang.Exception

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
    fun viewApplyVis(view: View, visibility: Int? = null) {
        when (visibility) {
            8, 4 -> { view.visibility = visibility; Animationz.fadeOutAnim(view)}
            else -> { view.visibility = visibility ?: 0; Animationz.fadeInAnim(view)}
        }
    }

}