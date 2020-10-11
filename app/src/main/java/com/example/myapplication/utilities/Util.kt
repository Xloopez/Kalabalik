package com.example.myapplication.utilities

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.utilities.Animationz.fadeInAnim1
import com.example.myapplication.utilities.Animationz.fadeOutAnim1
import com.example.myapplication.utilities.Animationz.slideOutRight

//object Util {

abstract class FragmentInputSettings(var fragmentManager: FragmentManager,
                            var fragment: Fragment,
                            var layoutId: Int,
                            @Nullable var tag: String? = "",
                            var replace: Boolean? = false,
                            var animate: Boolean? = false)

fun Boolean.runUnitTrue(uTrue: ()-> Unit) { if (this) uTrue() }
fun Boolean.runUnitTrueElse(uTrue: () -> Unit, uElse: () -> Unit) = if (this) { uTrue() } else { uElse() }

fun AppCompatButton.btnChangeText(text: String) = apply { this@btnChangeText.text = text }

fun Int.isZero() = (this == 0)
fun Int.isEqualTo(value: Int) = (this == value)

fun Array<String>.getRandomListIndex() = (0 until this.count()).random()

fun Iterable<() -> Unit>.runIterateUnit() {for (unit in this ) unit()}
fun Iterable<AppCompatButton>.clickable(clickable: Boolean) { for (element in this) element.isClickable = clickable }

fun MutableList<Unit>.runListUnits() = this.forEach { return@forEach it }
fun MutableList<*>.fromListSetViewsVis(vis: Int) {for (element in this.filterIsInstance<View>() ) element.visibility = vis }

fun Activity.hideSoftKeyBoard(view: View){
    (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken,0)
}

fun View.setViewVisibilityFadeInOut(visible: Boolean) {
    when (visible){
        true -> {
            apply {
                if(this.visibility != View.VISIBLE) {
                    visibility = View.VISIBLE
                    fadeInAnim1().start()
                }
            }
        }
        false -> {
            apply {
                if(this.visibility != View.INVISIBLE) {
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