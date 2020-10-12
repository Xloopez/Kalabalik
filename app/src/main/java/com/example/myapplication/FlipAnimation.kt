package com.example.myapplication

import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.example.myapplication.utilities.Animationz.flipToBackY
import com.example.myapplication.utilities.Animationz.flipToFrontY

class FlipAnimation(
	var view: View?,
	val flipCallBack: FlipAnimationInterface)
{


	fun startAnim(){

		view?.apply {
			val a1 = flipToBackY().apply {
				doOnStart {flipCallBack.oneOnStart()}
				doOnEnd {flipCallBack.oneOnEnd()}
			}

			val a2 = flipToFrontY().apply {
				doOnStart {flipCallBack.twoOnStart()}
				doOnEnd {flipCallBack.twoOnEnd()}
			}

			val a3 = flipToBackY().apply {
				doOnStart {flipCallBack.threeOnStart()}
				doOnEnd {flipCallBack.threeOnEnd()}
			}

			val a4 = flipToFrontY().apply {
				doOnStart {flipCallBack.fourOnStart()}
				doOnEnd {flipCallBack.fourOnEnd()}
			}


		}
	}

	interface FlipAnimationInterface{
		fun oneOnStart()
		fun oneOnEnd()
		fun twoOnStart()
		fun twoOnEnd()
		fun threeOnStart()
		fun threeOnEnd()
		fun fourOnStart()
		fun fourOnEnd()
	}


}
