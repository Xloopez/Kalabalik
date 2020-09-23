package com.example.myapplication

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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

    fun hideAllViews(mutableList: MutableList<*>){
        mutableList.filterIsInstance<View>().forEach { v -> v.visibility = View.INVISIBLE }
    }
    fun showAllViews(mutableList: MutableList<*>){
        mutableList.filterIsInstance<View>().forEach { v -> v.visibility = View.VISIBLE }
    }
    fun newFragmentInstance(fragmentManager: FragmentManager, fragment: Fragment, layoutId: Int, tag: String, replace: Boolean) {

        fragmentManager.beginTransaction().apply {

            when (replace) {
                true -> { replace(layoutId, fragment, tag) }
                false -> { add(layoutId, fragment, tag) }
            }
//            when (replace) {
//                true -> { addToBackStack("TAG") }
//                false -> { }
//            }

        }.commit()

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