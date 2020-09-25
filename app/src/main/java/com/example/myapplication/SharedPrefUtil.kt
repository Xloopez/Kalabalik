package com.example.myapplication

import android.app.Activity
import android.content.Context

class SharedPrefUtil(activity: Activity?) {

	private val sharedPref = activity?.getSharedPreferences("COM.SHAREDPREF", Context.MODE_PRIVATE)
	val res = activity?.resources

	fun putInt(resName: String, int: Int) = with (sharedPref!!.edit()) {
		putInt(resName, int)
		commit()
	}

	fun getInt(resName: Int): Int = sharedPref?.getInt(res?.getString(resName), 0) ?: 0

	fun putFloat(resName: String, float: Float) = with (sharedPref!!.edit()) {
		putFloat(resName, float)
		commit()
	}

	fun getFloat(resName: Int): Float = sharedPref?.getFloat(res?.getString(resName), 0f) ?: 0f


}