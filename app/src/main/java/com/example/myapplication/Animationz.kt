package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
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
            .translationX(-(Resources.getSystem().displayMetrics.widthPixels/2).toFloat() + (view.width))
            .scaleXBy(1.5f)
            .scaleYBy(1.5f)
            .start()

    }

    fun hideSoftKeyBoard(activity: Activity, view: View){
        val im = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(view.windowToken,0)
    }

    fun slideOutRightSlideInLeft(view: View) {

        val list = mutableListOf(slideOutRight(view), slideInLeft(view))

        AnimatorSet().apply {
            playSequentially(list[0], list[1])
        }.start()

    }

    fun flipCard(context: Context, view: View, textToSet: String, listOfView: MutableList<View>): AnimatorSet {

        Util.viewApplyVis(view, View.VISIBLE)
        val listFilterButtons = listOfView.filterIsInstance<AppCompatButton>()


        val firstTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 90f)
            .apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                doOnStart {
                    listFilterButtons.forEach {
                        it.apply {
                            it.isClickable = false
                            animate().rotationBy(100f)
                        }
                    }
                }
                view.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)
                
            doOnEnd {
                view.apply {
                   // text = ""
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
                    listFilterButtons.forEach { it.apply { animate().rotationBy(-100f) } }
                    view.animate().scaleXBy(0.5f).scaleYBy(0.5f)
                }
            }

        val fourthTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, -90f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 300
                doOnEnd {
                   // view.text = textToSet
                    listFilterButtons.forEach { it.apply { it.isClickable = true } }
                }
            }

        val scale: Float = context.resources.displayMetrics.density * 16000
        view.cameraDistance = scale

        return AnimatorSet().apply {
           playSequentially(firstTurn90, secondTurn90, thirdTurn90, fourthTurn90)
        }
    }


    fun flipCardFragment(context: Context, view: View, viewsList: MutableList<View>, gamingViewModel: GamingViewModel): AnimatorSet {

        val listFilterButtons = viewsList.filterIsInstance<AppCompatButton>()

        gamingViewModel.clearCardFragment.postValue(1)
        Util.viewApplyVis(view, View.VISIBLE)
        5
        val firstTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 90f)
            .apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                doOnStart {

                    view.background = ContextCompat.getDrawable(context, R.drawable.card_background_with_strokes)
                    listFilterButtons.forEach {
                        it.apply {
                            it.isClickable = false
                            animate().rotationBy(100f)
                        }
                    }
                }
                view.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)

                doOnEnd {
                    view.apply {
                        // text = ""

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
                    gamingViewModel.updateCardFragment.postValue(1)
                    view.background = ContextCompat.getDrawable(context, R.drawable.card_background_front)
                    listFilterButtons.forEach { it.apply { animate().rotationBy(-100f) } }
                    view.animate().scaleXBy(0.5f).scaleYBy(0.5f)
                }
            }

        val fourthTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, -90f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 300
                doOnEnd {

                    // view.text = textToSet
                    listFilterButtons.forEach { it.apply { it.isClickable = true } }
                }
            }

        val scale: Float = context.resources.displayMetrics.density * 8000
        view.cameraDistance = scale

        return AnimatorSet().apply {
            playSequentially(firstTurn90, secondTurn90, thirdTurn90, fourthTurn90)
        }
    }









}