package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.utilities.Animationz.fadeInAnim1
import com.example.myapplication.utilities.Animationz.fadeOutAnim1
import com.example.myapplication.utilities.Animationz.slideOutRight

abstract class FragmentInputSettings(
    var fragmentManager: FragmentManager,
    var fragment: Fragment,
    var layoutId: Int,
    @Nullable var tag: String? = "",
    var replace: Boolean? = false,
    var animate: Boolean? = false
)


enum class EnOperation { SUCCESS, FAIL; }
enum class EnScore { MINI, FINAL; }

enum class EnRandom {
    CONSEQUENCES,
    MISSION;

    fun getEnumString(): String = when (this) {
        CONSEQUENCES -> "CONSEQUENCE"
        MISSION -> "MISSION"
    }
}

fun AppCompatButton.btnChangeText(text: String) = apply { this@btnChangeText.text = text }

fun Int.isZero() = (this == 0)
fun Int.isEqualTo(value: Int) = (this == value)

fun MutableList<String>.getRandomListIndex() = (0 until this.count()).random()

fun Iterable<() -> Unit>.runIterateUnit() {
    for (unit in this) unit()
}

fun Iterable<AppCompatButton>.clickable(clickable: Boolean) {
    for (element in this) element.isClickable = clickable
}

fun playSound(context: Context, @RawRes rawRes: Int) {

    MediaPlayer.create(context, rawRes)
        .apply {
            setOnCompletionListener {
                it?.release()
            }
            setOnErrorListener { mp, _, _ ->
                with(mp) {
                    if (isPlaying) {
                        stop()
                    }
                    reset()
                    release()
                    false
                }
            }
        }.start()

}

//fun Boolean.runUnitTrue(uTrue: ()-> Unit) { if (this) uTrue() }
//fun Boolean.runUnitTrueElse(uTrue: () -> Unit, uElse: () -> Unit) = if (this) { uTrue() } else { uElse() }
//fun MutableList<Unit>.runListUnits() = this.forEach { return@forEach it }
//fun MutableList<*>.fromListSetViewsVis(vis: Int) {for (element in this.filterIsInstance<View>() ) element.visibility = vis }

fun Activity.hideSoftKeyBoard(view: View) {
    (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view.windowToken,
        0
    )
}

fun View.setViewVisibilityFadeInOut(visible: Boolean) {
    when (visible) {
        true -> {
            apply {
                if (this.visibility != View.VISIBLE) {
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

fun FragmentInputSettings.newFragmentInstance(context: Context? = null): FragmentTransaction {

    val f = this
//    context?.let {  playSound(it, R.raw.trail_swoosh) }

    return f.fragmentManager.beginTransaction().apply {

        f.apply {
            when (animate) {
                true -> {
                    setCustomAnimations(
                        R.anim.fragment_slide_right_enter,
                        R.anim.fragment_slide_left_exit
                    )
                }
            }

            when (f.replace) {
                true -> { replace(layoutId, fragment, tag)
                }
                false -> { add(layoutId, fragment, tag)
                }
            }
        }
    }
}