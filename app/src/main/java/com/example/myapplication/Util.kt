package com.example.myapplication

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.Animationz.fadeInAnim1
import com.example.myapplication.Animationz.fadeOutAnim1

object Util {

    fun setViewVisibilityFadeInOut(view: View, visible: Boolean) {
        when (visible){
            true -> {
                view.apply {
                    visibility = View.VISIBLE
                    fadeInAnim1().start()
                }
            }
            false -> {
                view.apply {
                    visibility = View.INVISIBLE
                    fadeOutAnim1().start()
                }
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

        //TODO BOOLEAN ANIM??
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

    fun newFragmentInstanceAnim(fragmentManager: FragmentManager,
                            fragment: Fragment,
                            layoutId: Int,
                            tag: String,
                            replace: Boolean,
                            anim: Boolean): FragmentTransaction {

        //TODO BOOLEAN ANIM??
        return fragmentManager.beginTransaction().apply {

            when (anim) {
                true -> {
                    setCustomAnimations(
                        R.anim.fragment_slide_right_enter,
                        R.anim.fragment_slide_left_exit
                    )
                }
                false -> { }
            }

            when (replace) {
                true -> { replace(layoutId, fragment, tag) }
                false -> { add(layoutId, fragment, tag) }
            }

        }

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