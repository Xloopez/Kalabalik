package com.example.myapplication.gaming

import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatTextView

abstract class SecondsTimer(
    totalRunningSeconds: Long,
    private val updateInterval: Long,
    private val textView: AppCompatTextView,
    private val tCallBack: TimerCallBack
) : CountDownTimer((totalRunningSeconds * 1000), (updateInterval * 1000)) {

    override fun onTick(p0: Long) {
        textView.updateTextViewOnTick(getSeconds(p0).toString())
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