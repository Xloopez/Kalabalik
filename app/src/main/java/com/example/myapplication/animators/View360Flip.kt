package com.example.myapplication.animators

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart

class View360Flip(
	var view: View?,
	var totalDuration: Long,
	var interpolators: ArrayList<Interpolator> = arrayListOf(
		LinearInterpolator(),
		LinearInterpolator(),
		LinearInterpolator(),
		LinearInterpolator()
	),
	val flipCallBack: FlipAnimationInterface
) {
	fun getAnimatorSet(): AnimatorSet {

		var a1: ObjectAnimator? = null
		var a2: ObjectAnimator? = null
		var a3: ObjectAnimator? = null
		var a4: ObjectAnimator? = null

		view?.apply {
			a1 = flipToBackY(d = totalDuration.div(4)).apply {
				interpolator = interpolators[0]

				doOnStart { flipCallBack.firstFourthStart() }
				doOnEnd { flipCallBack.firstFourthEnd() }
			}

			a2 = flipToFrontY(d = totalDuration.div(4)).apply {
				interpolator = interpolators[1]
				doOnStart { flipCallBack.secondFourthOnStart() }
				doOnEnd { flipCallBack.secondFourthOnEnd() }
			}

			a3 = flipToBackY(d = totalDuration.div(4)).apply {
				interpolator = interpolators[2]
				doOnStart { flipCallBack.thirdFourthOnStart() }
				doOnEnd { flipCallBack.thirdFourthOnEnd() }
			}

			a4 = flipToFrontY(d = totalDuration.div(4)).apply {
				interpolator = interpolators[3]
				doOnStart { flipCallBack.fourthFourthOnStart() }
				doOnEnd { flipCallBack.fourthFourthOnEnd() }
			}
		}
		return AnimatorSet().apply { playSequentially(a1, a2, a3, a4) }
	}

	private fun View.flipToBackY(d: Long? = 800L): ObjectAnimator {
		return ObjectAnimator.ofFloat(this, View.ROTATION_Y, 0f, 90f).apply {
			duration = d ?: 800L
		}
	}

	private fun View.flipToFrontY(d: Long? = 800L): ObjectAnimator {
		return ObjectAnimator.ofFloat(this, View.ROTATION_Y, -90f, 0f).apply {
			duration = d ?: 800L
		}
	}

	interface FlipAnimationInterface {
		fun firstFourthStart() {}
		fun firstFourthEnd() {}
		fun secondFourthOnStart() {}
		fun secondFourthOnEnd() {}
		fun thirdFourthOnStart() {}
		fun thirdFourthOnEnd() {}
		fun fourthFourthOnStart() {}
		fun fourthFourthOnEnd() {}
	}

}
