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
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat


object Animationz {

    private const val flipCardDuration = 1200L
    private const val flipCardDurationOneHalf = flipCardDuration/2
    private const val flipCardDurationOneThird = flipCardDuration/3
    private const val flipCardDurationOneFourth = flipCardDuration/4



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

    fun hideSoftKeyBoard(activity: Activity, view: View){
        val im = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(view.windowToken,0)
    }

    fun View.slideOutRightSlideInLeft(): AnimatorSet {

        val list = mutableListOf(
            slideOutRight(this),
            slideInLeft(this))

        return AnimatorSet().apply {
            playSequentially(list[0], list[1])
        }

    }

    fun AppCompatTextView.slideOutRightInLeftSetText(sText: String): AnimatorSet {

        val anim1 = slideOutRight(this)
        val anim2 = slideInLeft(this)

        anim1.apply {
            duration = flipCardDurationOneThird
        }.doOnEnd { _ ->
            this.text = sText
        }
        anim2.apply { duration = flipCardDurationOneThird }

        return AnimatorSet().apply {
            playSequentially(anim1, anim2)
        }

    }

    private fun View.flipView(context: Context): AnimatorSet {

        val scale: Float = context.resources.displayMetrics.density * 8000
        checkCameraDistance(this, scale)

        val firstTurn90 = ObjectAnimator.ofFloat(this, View.ROTATION_Y, 0f, 90f).apply {
            duration = flipCardDurationOneHalf
        }
        val secondTurn90 = ObjectAnimator.ofFloat(this, View.ROTATION_Y, -90f, 0f).apply {
            duration = flipCardDurationOneHalf
        }

        return AnimatorSet().apply {
            playSequentially(firstTurn90, secondTurn90)
        }

    }

    fun flipCardFragment(context: Context, view: View, viewsList: MutableList<View>, gamingViewModel: GamingViewModel): AnimatorSet {

        val listFilterButtons = viewsList.filterIsInstance<AppCompatButton>()

        gamingViewModel.clearCardFragment.postValue(1)
        Util.viewApplyVis(view, View.VISIBLE)

        val scale: Float = context.resources.displayMetrics.density * 16000
        checkCameraDistance(view, scale)

        val firstTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 90f)
            .apply {
                duration = flipCardDurationOneFourth
                interpolator = DecelerateInterpolator()
                doOnStart {

                    view.background = ContextCompat.getDrawable(context, R.drawable.card_background_with_strokes)
                    listFilterButtons.forEach {
                        it.apply {
                            isClickable = false
                            flipView(context = context).start()
                        }
                    }
                }
                //view.animate().scaleXBy(-0.5f).scaleYBy(-0.5f)

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
                duration = flipCardDurationOneFourth
            }

        val thirdTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 90f)
            .apply {
                duration = flipCardDurationOneFourth
                interpolator = DecelerateInterpolator()

                doOnEnd {
                    gamingViewModel.updateCardFragment.postValue(1)
                    view.background = ContextCompat.getDrawable(context, R.drawable.card_background_front)
                  //  listFilterButtons.forEach { it.apply { animate().rotationBy(-100f) } }
                }
            }

        val fourthTurn90 = ObjectAnimator.ofFloat(view, View.ROTATION_Y, -90f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = flipCardDurationOneFourth
                doOnEnd {

                    // view.text = textToSet
                    listFilterButtons.forEach { it.apply { it.isClickable = true } }
                }
            }



        return AnimatorSet().apply {
            playSequentially(firstTurn90, secondTurn90, thirdTurn90, fourthTurn90)
        }
    }

    private fun checkCameraDistance(view: View, scale: Float) {
        when (view.cameraDistance) {
            scale -> {
            }
            else -> {
                view.cameraDistance = scale
            }

        }
    }


}