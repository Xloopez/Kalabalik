package com.example.myapplication.gaming

import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat.animate

abstract class SecondsTimer(
    totalRunningSeconds: Long,
    private val updateInterval: Long,
    private val textView: AppCompatTextView,
    private val tCallBack: TimerCallBack
) : CountDownTimer((totalRunningSeconds * 1000), (updateInterval * 1000)) {

    private val pulsateUp
        get() = animate(textView).scaleXBy(0.5f).scaleYBy(0.5f).apply { duration = (1000 / 2) }
    private val pulsateDown
        get() = animate(textView).scaleXBy(-0.5f).scaleYBy(-0.5f).apply { duration = (1000 / 2) }

    override fun onTick(p0: Long) {
        textView.apply {
            updateTextViewOnTick(getSeconds(p0).toString())
            visibility = View.VISIBLE

            pulsateUp.withEndAction {
                pulsateDown.start()
            }.start()

        }
    }

    override fun onFinish() {
        tCallBack.onFinish()
    }

    interface TimerCallBack {
        fun onFinish()
    }

    private fun getSeconds(p0: Long): Long { return p0.div((updateInterval.times(1000))) }

    private fun AppCompatTextView.updateTextViewOnTick(sText: String){
        this.post {
            text = sText
        }
    }

}