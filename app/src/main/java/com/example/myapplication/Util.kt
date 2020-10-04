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

//object Util {

abstract class FragmentInputSettings(var fragmentManager: FragmentManager,
                            var fragment: Fragment,
                            var layoutId: Int,
                            @Nullable var tag: String? = "",
                            var replace: Boolean? = false,
                            var animate: Boolean? = false){}


inline fun MutableList<Unit>.viewApplyVisFromList() = this.forEach { return@forEach it }
inline fun Int.isZero() = (this == 0)
inline fun Int.isEqualTo(value: Int) = (this == value)
fun MutableList<*>.hideAllViews() { filterIsInstance<View>().forEach { v -> v.visibility = View.INVISIBLE } }
fun MutableList<*>.showAllViews() { filterIsInstance<View>().forEach { v -> v.visibility = View.VISIBLE } }

fun Activity.hideSoftKeyBoard(view: View){
    val im = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(view.windowToken,0)
}

fun View.setViewVisibilityFadeInOut(visible: Boolean) {
    when (visible){
        true -> {
            apply {
                if(this@setViewVisibilityFadeInOut.visibility != View.VISIBLE) {
                    visibility = View.VISIBLE
                    fadeInAnim1().start()
                }
            }
        }
        false -> {
            apply {
                if(this@setViewVisibilityFadeInOut.visibility != View.INVISIBLE) {
                    visibility = View.INVISIBLE
                    fadeOutAnim1().start()
                }
            }
        }
    }
}

fun View.viewApplyVis(visibility: Int? = null) {
     when (visibility) {
        8, 4 -> { this.visibility = visibility; slideOutRight() }
        else -> { this.visibility = visibility ?: 0; slideOutRight() }
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


//}