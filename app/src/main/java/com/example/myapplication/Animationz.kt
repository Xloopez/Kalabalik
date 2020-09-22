package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat


object Animationz {

    fun fadeInAnim(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 0.1f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }

    fun fadeOutAnim(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0.1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }

    fun slideOutRight(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, view.width + 100f).apply {
            interpolator = AccelerateInterpolator()
            duration = 300
        }
    }

    fun slideInLeft(view: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -view.width - 100f, 0f).apply {
            interpolator = DecelerateInterpolator()
            duration = 300
        }
    }

    fun animButton(view: View){

        val translateYAmount = (+ view.height + 150f)
        view.animate()
            .translationYBy(translateYAmount)
            .scaleXBy(1.5f)
            .scaleYBy(1.5f)
            .start()

    }

    fun slideOutRightSlideInLeft(view: View) {

        val list = mutableListOf(slideOutRight(view), slideInLeft(view))

        AnimatorSet().apply {
            playSequentially(list[0], list[1])
        }.start()

    }

    fun flipCard(context: Context, view: AppCompatTextView, textToSet: String, listOfView: MutableList<View>): AnimatorSet {

        Util.viewApplyVis(view, View.VISIBLE)
       // val listFilterButtons = listOfView.filterIsInstance<AppCompatButton>()


        val firstTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 90f)
            .apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                doOnStart {
//                    listFilterButtons.forEach {
//                        it.apply {
//                            it.isClickable = false
//                            animate().rotationBy(100f)
//                        }
//                    }
                }
                view.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)
                
            doOnEnd {
                view.apply {
                    text = ""
                    background = ContextCompat.getDrawable(context, R.drawable.card_background_with_strokes)
                    //background = ContextCompat.getDrawable(context, R.drawable.card_background)
                }
            }
        }

        val secondTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, -90f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 300
            }

        val thirdTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 90f)
            .apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                doOnEnd {
                    view.background = ContextCompat.getDrawable(context, R.drawable.card_background_front)
//                    listFilterButtons.forEach { it.apply { animate().rotationBy(-100f) } }
                    view.animate().scaleXBy(0.5f).scaleYBy(0.5f)
                }
            }

        val fourthTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, -90f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 300
                doOnEnd {
                    view.text = textToSet
//                    listFilterButtons.forEach { it.apply { it.isClickable = true } }
                }
            }

        val scale: Float = context.resources.displayMetrics.density * 16000
        view.cameraDistance = scale

        return AnimatorSet().apply {
           playSequentially(firstTurn90, secondTurn90, thirdTurn90, fourthTurn90)
        }
    }











}