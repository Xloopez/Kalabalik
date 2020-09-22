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
    fun viewApplyVis(view: View, visibility: Int? = null) {
        when (visibility) {
            8, 4 -> {
                view.visibility = visibility; Animationz.slideOutRight(view)
            }
            else -> { view.visibility = visibility ?: 0; Animationz.slideInLeft(view)}
        }
    }

    fun viewApplyVisFromList(mutableList: MutableList<Unit>) {
        mutableList.forEach { it.apply {  } }
    }


//    fun disableViewClickTemp(view: View) {
//
//        view.isEnabled = false
//
//        val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
//        val runnable = Runnable {
//            view.isEnabled = true
//        }
//        executor.schedule(runnable, 2, TimeUnit.SECONDS)
//    }


}