package com.example.myapplication

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.Animationz.fadeInAnim1
import com.example.myapplication.Animationz.fadeOutAnim1
import com.example.myapplication.Animationz.slideOutRight

object Util {

    abstract class FragmentInputSettings(var fragmentManager: FragmentManager,
                                var fragment: Fragment,
                                var layoutId: Int,
                                @Nullable var tag: String? = "",
                                var replace: Boolean? = false,
                                var animate: Boolean? = false){}

    fun Activity.hideSoftKeyBoard(view: View){
        val im = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(view.windowToken,0)
    }

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
                view.visibility = visibility; view.slideOutRight()
            }
            else -> { view.visibility = visibility ?: 0; view.slideOutRight()}
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